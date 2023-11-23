package top.kthirty.core.db.dict;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.db.support.SqlKeyword;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.DictItem;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.Comparator;
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
public class DefaultDictProvider implements DictProvider {
    private final RedisUtil redisUtil;
    private final KthirtyDictProperties dictProperties;
    private final Cache<String, List<DictItem>> cache = CacheUtil.newLFUCache(10000);

    @Override
    public void removeCache(String code, String value) {
        if(Func.isAnyBlank(code,value)){
            return;
        }
        // 删除对应的Item
        if(Func.notNull(value)){
            List<DictItem> items = get(code);
            if (Func.isNotEmpty(items)) {
                List<DictItem> list = items.stream().filter(i -> Func.equalsSafe(i.getValue(), value)).toList();
                put(code, list, redisUtil.getExpire(code));
            }
            return;
        }
        // 删除所有
        if (Func.notNull(redisUtil)) {
            redisUtil.del(dictProperties.getRedisKeyPrefix() + code);
        } else {
            cache.remove(dictProperties.getRedisKeyPrefix() + code);
        }
    }

    @Override
    public synchronized void put(String code, List<DictItem> items, long time) {
        if(Func.isBlank(code) || Func.isEmpty(items)){
            return ;
        }
        items.forEach(item -> item.setCode(code));
        items = CollUtil.sort(items, Comparator.comparingInt(DictItem::getWeight));
        String finalCode = dictProperties.getRedisKeyPrefix() + code;
        if (Func.notNull(redisUtil)) {
            redisUtil.set(finalCode,items,time);
        } else {
            cache.put(finalCode,items,time * 1000);
        }
    }

    @Override
    public synchronized List<DictItem> get(String code) {
        if(Func.isBlank(code)){
            return List.of();
        }
        // 读缓存
        String finalCode = dictProperties.getRedisKeyPrefix() + code;
        if(Func.notNull(redisUtil)){
            if(redisUtil.hasKey(finalCode)){
                return redisUtil.get(finalCode);
            }
        }else{
            if(cache.containsKey(finalCode)){
                return cache.get(finalCode);
            }
        }
        // 配置不查库
        if(!dictProperties.isAutoDb()){
            return null;
        }
        // 查库
        if(isTableCode(code)){
            List<String> strings = StrUtil.splitTrim(code, StringPool.COLON);
            String tableName = strings.get(0);
            String valueField = strings.get(1);
            String labelField = strings.get(2);
            String sqlTemplate;
            if(hasField(tableName,"deleted")){
                sqlTemplate = "select {labelField} as \"label\",{valueField} as \"value\",deleted enabled from {tableName}";
            }else{
                sqlTemplate = "select {labelField} as \"label\",{valueField} as \"value\" from {tableName}";
            }
            Dict params = Dict.create()
                    .set("labelField", SqlKeyword.filter(labelField))
                    .set("tableName", SqlKeyword.filter(tableName))
                    .set("valueField", SqlKeyword.filter(valueField));
            String sql = StrUtil.format(sqlTemplate, params);
            List<Row> rows = Db.selectListBySql(sql);
            if(Func.notNull(rows)){
                return rows.stream().map(r -> r.toEntity(DictItem.class)).toList();
            }else{
                log.warn("字典解析器(表)查询数据库未找到任何可用信息 {}",sql);
            }
        }else{
            List<Row> rows = Db.selectListBySql(dictProperties.getDictSql());
            if(Func.notNull(rows)){
                return rows.stream().map(r -> r.toEntity(DictItem.class)).toList();
            }else{
                log.warn("字典解析器查询数据库未找到任何可用信息 {}",dictProperties.getDictSql());
            }
        }
        // 5分钟内不再查数据库，避免缓存击穿
        List<DictItem> list = ListUtil.list(true);
        put(code,list,5*60);
        return list;
    }

    private boolean isTableCode(String code){
        return Func.isNotBlank(code)
                && StrUtil.count(code, StringPool.COLON) == 2
                && StrUtil.splitTrim(code,StringPool.COLON).size() == 3;
    }


    private boolean hasField(String tableName,String field){
        try{
            Db.selectListBySql(StrUtil.format("select {} from {} where 1 = 2",field,tableName));
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
