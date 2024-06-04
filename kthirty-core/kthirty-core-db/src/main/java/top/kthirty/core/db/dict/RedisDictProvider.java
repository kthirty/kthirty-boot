package top.kthirty.core.db.dict;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.constant.SqlOperator;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.row.Db;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.DictItem;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.support.RequestVariableHolder;
import top.kthirty.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class RedisDictProvider implements DictProvider {
    private final KthirtyDictProperties dictProperties;

    @Override
    public void removeCache(String code) {
        RedisUtil.del(dictProperties.getCacheKeyPrefix() + code);
    }

    @Override
    public void put(String code, List<DictItem> items, long time) {
        RedisUtil.set(code, items, time);
    }

    @Override
    public List<DictItem> get(String code) {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        String finalCode = dictProperties.getCacheKeyPrefix() + code;
        Assert.isTrue(Util.getTableInfo(code) == null, "数据库表不支持查询所有选项");
        // 存在本地缓存
        if (RequestVariableHolder.has(finalCode)) {
            return RequestVariableHolder.get(finalCode);
        }
        // 存在Redis缓存
        if (Boolean.TRUE.equals(RedisUtil.hasKey(finalCode))) {
            List<DictItem> items = RedisUtil.get(finalCode);
            RequestVariableHolder.add(finalCode,items);
            log.info("从Redis中获取缓存{} {}",finalCode,timer.intervalPretty());
            return items;
        }
        // 近期查询过且为结果为空
        if (Boolean.TRUE.equals(RedisUtil.hasKey("empty:" + finalCode))) {
            return ListUtil.empty();
        }
        // 已配置不查询数据库
        if (!dictProperties.isAutoDb() || Func.isBlank(dictProperties.getDictSql())) {
            return ListUtil.empty();
        }
        // 开始查询数据库
        List<DictItem> list = new ArrayList<>();
        try {
            list = Db.selectListBySql(dictProperties.getDictSql(), code).stream().map(it -> {
                DictItem entity = it.toEntity(DictItem.class);
                entity.setParentId(StrUtil.blankToDefault(entity.getParentId(), Constant.ROOT_ID));
                return entity;
            }).toList();
        } catch (Throwable throwable) {
            log.warn("数据字典服务异常，自动查询数据库异常", throwable);
        }
        // 查询结果为空,防止缓存击穿
        if (CollUtil.isEmpty(list)) {
            RedisUtil.set("empty:" + finalCode, ListUtil.empty(), 5 * 60);
            return ListUtil.empty();
        }
        // 树形数据处理
        boolean isTree = list.stream().anyMatch(it -> !it.getParentId().equals(Constant.ROOT_ID));
        if (isTree) {
            list = Util.listToTree(list);
        }
        RedisUtil.set(finalCode, list, dictProperties.getCacheTime());
        RequestVariableHolder.add(finalCode,list);
        return list;
    }

    @Override
    public String getLabel(String code, String value, String separator) {
        if (StrUtil.hasBlank(code, value)) {
            return StringPool.EMPTY;
        }
        Util.TableInfo tableInfo = Util.getTableInfo(code);
        // 非数据库表
        if (tableInfo == null) {
            List<DictItem> result = new ArrayList<>();
            List<DictItem> dictItems = get(code);
            Util.deepGet(dictItems, it -> StrUtil.splitTrim(value, separator).contains(it.getValue()), result);
            return result.stream().map(DictItem::getLabel).collect(Collectors.joining(separator));
        }
        // 已配置不查库
        if(!dictProperties.isAutoDb()){
            return StringPool.EMPTY;
        }
        return Util.queryDb(dictProperties.getCacheKeyPrefix(), dictProperties.getCacheTime(), tableInfo, value, separator, true);
    }

    @Override
    public String getValue(String code, String label, String separator) {
        if (StrUtil.hasBlank(code, label)) {
            return StringPool.EMPTY;
        }
        Util.TableInfo tableInfo = Util.getTableInfo(code);
        // 非数据库表
        if (tableInfo == null) {
            List<DictItem> result = new ArrayList<>();
            List<DictItem> dictItems = get(code);
            Util.deepGet(dictItems, it -> StrUtil.splitTrim(label, separator).contains(it.getValue()), result);
            return result.stream().map(DictItem::getValue).collect(Collectors.joining(separator));
        }
        // 已配置不查库
        if(!dictProperties.isAutoDb()){
            return StringPool.EMPTY;
        }
        return Util.queryDb(dictProperties.getCacheKeyPrefix(),dictProperties.getCacheTime(),tableInfo,label,separator,false);
    }


    static class Util {
        private static void deepGet(List<DictItem> items, Func1<DictItem, Boolean> filter, List<DictItem> result) {
            if (Func.isNotEmpty(items)) {
                List<DictItem> list = items.stream().filter(filter::callWithRuntimeException).toList();
                if (Func.isNotEmpty(list)) {
                    list.forEach(item -> deepGet(item.getChildren(), filter, result));
                    result.addAll(list);
                }
            }
        }

        private static TableInfo getTableInfo(String code) {
            if (Func.isNotBlank(code)
                    && StrUtil.count(code, StringPool.COLON) == 2
                    && StrUtil.splitTrim(code, StringPool.COLON).size() == 3) {
                List<String> strings = StrUtil.splitTrim(code, StringPool.COLON);
                return new TableInfo(strings.get(0), strings.get(1), strings.get(2));
            } else {
                return null;
            }
        }

        private static List<DictItem> listToTree(List<DictItem> items) {
            return getChildren("0", items);
        }

        private static List<DictItem> getChildren(String parentId, List<DictItem> all) {
            if (Func.isNotEmpty(all)) {
                return all.stream()
                        .filter(i -> Func.equalsSafe(i.getParentId(), parentId))
                        .peek(i -> i.setChildren(getChildren(i.getId(), all)))
                        .sorted(Comparator.comparingInt(DictItem::getWeight))
                        .toList();
            }
            return null;
        }

        /**
         * 查询数据库字段名
         * @param keyPrefix 缓存Key前缀
         * @param cacheTime 缓存时长
         * @param tableInfo 查询表信息
         * @param text 值或标签文本
         * @param separator 分隔符
         * @param valueQueryLabel 是否为“使用值查询标签”
         * @return 标签或者值
         */
        private static String queryDb(String keyPrefix,long cacheTime, TableInfo tableInfo, String text, String separator, boolean valueQueryLabel) {
            String finalCode = keyPrefix + Func.join(":", "table", tableInfo.tableName, tableInfo.valueFieldName, tableInfo.labelFieldName);
            if (RedisUtil.hasKey(finalCode)) {
                return RedisUtil.get(finalCode);
            }
            // 开始查数据库
            boolean hasMultiple = StrUtil.splitTrim(text, separator).size() > 1;
            QueryCondition queryCondition = QueryCondition.create(
                    new QueryColumn(valueQueryLabel ? tableInfo.valueFieldName : tableInfo.labelFieldName),
                    hasMultiple ? SqlOperator.IN : SqlOperator.EQUALS,
                    hasMultiple ? StrUtil.splitTrim(text, separator) : text);
            String result = Db.selectListByCondition(tableInfo.tableName, queryCondition)
                    .stream()
                    .filter(it ->  Constant.NO.equals(it.getString("deleted",  Constant.NO)))
                    .map(it -> it.getString(valueQueryLabel ? tableInfo.labelFieldName : tableInfo.valueFieldName))
                    .collect(Collectors.joining(separator));
            RedisUtil.set(finalCode, result, cacheTime);
            log.info("查询数据库 {} {} {} {} {}",tableInfo.tableName,tableInfo.valueFieldName,tableInfo.labelFieldName,text,valueQueryLabel);
            return result;
        }

        @AllArgsConstructor
        static class TableInfo {
            /**
             * 表名
             */
            private String tableName;
            /**
             * value 字段名
             */
            private String valueFieldName;
            /**
             * label 字段名
             */
            private String labelFieldName;
        }
    }
}
