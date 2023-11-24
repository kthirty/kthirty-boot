package top.kthirty.core.db.dict;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.DictItem;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 默认字典获取器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/23
 */
@AllArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
public class DefaultDictProvider implements DictProvider {
    private final RedisUtil redisUtil;
    private final KthirtyDictProperties dictProperties;
    private final Cache<String, Object> cache = CacheUtil.newLFUCache(10000);

    @Override
    public synchronized void removeCache(String code, String value) {
        if (Func.isBlank(code)) {
            return;
        }
        // 删除对应的Item
        if (Func.isNotBlank(value)) {
            List<DictItem> items = get(code);
            if (Func.isNotEmpty(items)) {
                List<DictItem> filterItems = Util.deepFilter(items, i -> !Func.equalsSafe(i.getValue(), value));
                put(code, filterItems, dictProperties.getCacheTime());
            }
        } else {
            // 删除所有
            if (Func.notNull(redisUtil)) {
                redisUtil.del(dictProperties.getCacheKeyPrefix() + code);
            } else {
                cache.remove(dictProperties.getCacheKeyPrefix() + code);
            }
        }
    }

    @Override
    public synchronized void put(String code, List<DictItem> items, long time) {
        String finalCode = dictProperties.getCacheKeyPrefix() + code;
        if (Func.notNull(redisUtil)) {
            redisUtil.set(finalCode, items, time);
        } else {
            cache.put(finalCode, items, time * 1000);
        }
    }

    @Override
    public List<DictItem> get(String code) {
        Assert.hasText(code, "字典代码不可为空");
        String finalCode = dictProperties.getCacheKeyPrefix() + code;
        if (Func.notNull(redisUtil)) {
            if (redisUtil.hasKey(finalCode)) {
                return redisUtil.get(finalCode);
            }
            if(redisUtil.hasKey(Util.getEmptyKey(this,finalCode))){
                return null;
            }
        } else {
            if (cache.containsKey(finalCode)) {
                return (List<DictItem>)cache.get(finalCode);
            }
            if(cache.containsKey(Util.getEmptyKey(this,finalCode))){
                return null;
            }
        }
        // 不支持查库
        if (Func.isBlank(dictProperties.getDictSql())
                || !dictProperties.isAutoDb()
                || Util.isTableCode(code)) {
            return ListUtil.empty();
        }
        // 查库
        List<Row> rows = Db.selectListBySql(dictProperties.getDictSql(), code);
        if (Func.notNull(rows)) {
            List<DictItem> items = rows.stream().map(r -> r.toEntity(DictItem.class)).toList();
            // 空转0后，仍然存在父ID不为0的，说明为树状
            boolean isTree = items.stream()
                    .peek(i -> i.setParentId(StrUtil.blankToDefault(i.getParentId(), "0")))
                    .anyMatch(i -> !Func.equalsSafe(i.getParentId(), "0"));
            if (isTree) {
                items = Util.listToTree(items);
            }
            put(code, items, dictProperties.getCacheTime());
            return items;
        } else {
            // 短期不再查数据库，避免缓存击穿
            if (Func.notNull(redisUtil)) {
                redisUtil.set(Util.getEmptyKey(this,finalCode),StringPool.EMPTY,dictProperties.getEmptyCache());
            } else {
                cache.put(Util.getEmptyKey(this,finalCode),StringPool.EMPTY,dictProperties.getEmptyCache());
            }
            log.warn("字典解析器查询数据库未找到任何可用信息 [{}秒内不再查数据库，可清除缓存{}] {} {}", dictProperties.getEmptyCache(),code, dictProperties.getDictSql(), code);
            return null;
        }
    }

    @Override
    public String getLabel(String code, String value, String separator) {
        List<String> values = Func.isNotBlank(separator) ? StrUtil.split(value, separator) : List.of(value);
        List<String> result = new ArrayList<>();
        for (String item : values) {
            // 存在空缓存，跳过
            if (Util.contains(this, Util.getEmptyKey(this,code,value))) {
                break;
            }
            // 获取缓存（数据字典类型且配置了自动查库，get方法中会自动查库！！！）
            List<DictItem> items = get(code);
            if (Func.isNotEmpty(items)) {
                DictItem find = Util.deepGetFirst(items, i -> Func.equalsSafe(i.getValue(), value));
                if (Func.notNull(find)) {
                    result.add(find.getLabel());
                    break;
                }
            }
            // 缓存没有且配置了自动查询数据库,开始查库
            if (Util.isTableCode(code) && dictProperties.isAutoDb()) {
                try {
                    // 解析CODE
                    List<String> strings = StrUtil.splitTrim(code, StringPool.COLON);
                    String tableName = strings.get(0);
                    String valueField = strings.get(1);
                    String labelField = strings.get(2);
                    Row row = DbChain.table(tableName)
                            .select(labelField)
                            .where(new QueryColumn(valueField).eq(value))
                            .limit(1)
                            .one();
                    if(Func.isNotEmpty(row)){
                        // 已查询到信息，将字段存入结果并添加缓存
                        String label = row.getString(labelField);
                        result.add(label);
                        items = ObjectUtil.defaultIfNull(items,ListUtil.list(true));
                        items.add(DictItem.builder().label(label).value(value).build());
                        put(code,items,dictProperties.getCacheTime());
                    }else{
                        // 短期不再查数据库，避免缓存击穿
                        if (Func.notNull(redisUtil)) {
                            redisUtil.set(Util.getEmptyKey(this,code,value),StringPool.EMPTY,dictProperties.getEmptyCache());
                        } else {
                            cache.put(Util.getEmptyKey(this,code,value),StringPool.EMPTY,dictProperties.getEmptyCache());
                        }
                    }
                } catch (Exception e) {
                    log.error("查询数据库出错");
                }
            }
        }
        return CollUtil.join(result, StrUtil.blankToDefault(separator, StringPool.COMMA));
    }

    @Override
    public String getValue(String code, String label, String separator) {
        List<String> values = Func.isNotBlank(separator) ? StrUtil.split(label, separator) : List.of(label);
        List<String> result = new ArrayList<>();
        for (String item : values) {
            // 存在空缓存，跳过
            if (Util.contains(this, Util.getEmptyKey(this,code,label))) {
                break;
            }
            // 获取缓存（数据字典类型且配置了自动查库，get方法中会自动查库！！！）
            List<DictItem> items = get(code);
            if (Func.isNotEmpty(items)) {
                DictItem find = Util.deepGetFirst(items, i -> Func.equalsSafe(i.getValue(), label));
                if (Func.notNull(find)) {
                    result.add(find.getLabel());
                    break;
                }
            }
            // 缓存没有且配置了自动查询数据库,开始查库
            if (Util.isTableCode(code) && dictProperties.isAutoDb()) {
                try {
                    // 解析CODE
                    List<String> strings = StrUtil.splitTrim(code, StringPool.COLON);
                    String tableName = strings.get(0);
                    String valueField = strings.get(1);
                    String labelField = strings.get(2);
                    Row row = DbChain.table(tableName)
                            .select(valueField)
                            .where(new QueryColumn(labelField).eq(label))
                            .limit(1)
                            .one();
                    if(Func.isNotEmpty(row)){
                        // 已查询到信息，将字段存入结果并添加缓存
                        String value = row.getString(valueField);
                        result.add(value);
                        items = ObjectUtil.defaultIfNull(items,ListUtil.list(true));
                        items.add(DictItem.builder().label(label).value(value).build());
                        put(code,items,dictProperties.getCacheTime());
                    }else{
                        // 短期不再查数据库，避免缓存击穿
                        if (Func.notNull(redisUtil)) {
                            redisUtil.set(Util.getEmptyKey(this,code,label),StringPool.EMPTY,dictProperties.getEmptyCache());
                        } else {
                            cache.put(Util.getEmptyKey(this,code,label),StringPool.EMPTY,dictProperties.getEmptyCache());
                        }
                    }
                } catch (Exception e) {
                    log.error("查询数据库出错");
                }
            }
        }
        return CollUtil.join(result, StrUtil.blankToDefault(separator, StringPool.COMMA));
    }


    private static class Util {
        public static boolean contains(DefaultDictProvider provider,String key){
            if (Func.notNull(provider.redisUtil)) {
                return provider.redisUtil.hasKey(key);
            } else {
                return provider.cache.containsKey(key);
            }
        }
        public static String getEmptyKey(DefaultDictProvider provider,String... key){
            return Func.join(StringPool.COLON
                    ,"empty"
                    ,provider.dictProperties.getCacheKeyPrefix()
                    ,Func.join(StringPool.COLON,List.of(key))
            );
        }

        /**
         * 深度获取所有匹配项
         */
        private static void deepGet(List<DictItem> items, Func1<DictItem, Boolean> filter, List<DictItem> result) {
            if (Func.isNotEmpty(items)) {
                List<DictItem> list = items.stream().filter(filter::callWithRuntimeException).toList();
                if (Func.isNotEmpty(list)) {
                    list.forEach(item -> deepGet(item.getChildren(), filter, result));
                    result.addAll(list);
                }
            }
        }

        /**
         * 是否为数据库格式CODE
         */
        private static boolean isTableCode(String code) {
            return Func.isNotBlank(code)
                    && StrUtil.count(code, StringPool.COLON) == 2
                    && StrUtil.splitTrim(code, StringPool.COLON).size() == 3;
        }

        /**
         * 深度筛选
         */
        private static List<DictItem> deepFilter(List<DictItem> items, Func1<DictItem, Boolean> filter) {
            if (Func.isEmpty(items)) {
                return ListUtil.list(true);
            }
            return items.stream()
                    .filter(filter::callWithRuntimeException)
                    .peek(item -> {
                        if (Func.isNotEmpty(item.getChildren())) {
                            item.setChildren(deepFilter(item.getChildren(), filter));
                        }
                    })
                    .toList();
        }

        /**
         * 深度查找第一个匹配项
         */
        private static DictItem deepGetFirst(List<DictItem> items, Func1<DictItem, Boolean> filter) {
            for (DictItem item : items) {
                if (filter.callWithRuntimeException(item)) {
                    return item;
                }
                if (Func.isNotEmpty(item.getChildren())) {
                    DictItem dictItem = deepGetFirst(item.getChildren(), filter);
                    if (dictItem != null) {
                        return null;
                    }
                }
            }
            return null;
        }

        private static List<DictItem> listToTree(List<DictItem> items) {
            return getChildren("0", items);
        }

        private static List<DictItem> getChildren(String parentId, List<DictItem> all) {
            if (Func.isNotEmpty(all)) {
                return all.stream()
                        .filter(i -> Func.equalsSafe(i.getParentId(), parentId))
                        .peek(i -> i.setChildren(getChildren(i.getId(), all)))
                        .toList();
            }
            return null;
        }
    }
}
