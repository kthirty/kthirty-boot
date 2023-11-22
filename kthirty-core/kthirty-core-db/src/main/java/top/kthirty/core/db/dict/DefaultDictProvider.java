package top.kthirty.core.db.dict;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.db.support.SqlKeyword;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.List;

/**
 * Redis 字典获取器
 */
@AllArgsConstructor
@Slf4j
public class DefaultDictProvider implements DictProvider {
    private final RedisUtil redisUtil;
    private final KthirtyDictProperties dictProperties;
    private final Cache<String, String> cache = CacheUtil.newLFUCache(10000);

    @Override
    public String getLabel(String code, String value) {
        String cacheKey = dictProperties.getRedisKeyPrefix() + code + StringPool.COLON + value;
        String cacheValue = getCache(cacheKey);
        if(Func.notNull(cacheValue)){
            return cacheValue;
        }
        String label = getBySql(dictProperties.getDictSql(), code, value);
        return putCache(cacheKey,label,dictProperties.getCacheTime());
    }

    @Override
    public String getLabel(String tableName, String valueField, String labelField, String value) {
        String cacheKey = dictProperties.getRedisKeyPrefix() + Func.join(StringPool.UNDERSCORE,tableName,valueField,labelField) + StringPool.COLON + value;
        String cacheValue = getCache(cacheKey);
        if(Func.notNull(cacheValue)){
            return cacheValue;
        }

        String sqlTemplate = "select {labelField} from {tableName} where {valueField} = '?'";
        Dict params = Dict.create()
                .set("labelField", SqlKeyword.filter(labelField))
                .set("tableName", SqlKeyword.filter(tableName))
                .set("valueField", SqlKeyword.filter(valueField));
        String sql = StrUtil.format(sqlTemplate, params);
        String label = getBySql(sql, value);
        return putCache(cacheKey,label,dictProperties.getCacheTime());
    }

    /**
     * 删除缓存，value 为空默认为删除所有
     * @param code 字典code
     * @param value 字典value
     */
    @Override
    public void removeCache(String code, String value) {
        if(redisUtil != null){
            if(Func.isBlank(value)){
                redisUtil.keys(dictProperties.getRedisKeyPrefix() + code + StringPool.COLON + StringPool.ASTERISK).forEach(redisUtil::del);
            }else {
                redisUtil.del(code + StringPool.COLON + value);
            }
        }else{
            if(Func.isBlank(value)){
                cache.cacheObjIterator().forEachRemaining(item -> {
                    if(StrUtil.startWith(item.getKey(),code)){
                        cache.remove(item.getKey());
                    }
                });
            }else{
                cache.remove(code + StringPool.COLON + value);
            }
        }

    }

    private String getBySql(String sql, Object... args){
        try {
            List<Row> rows = Db.selectListBySql(sql, args);
            if (Func.isNotEmpty(rows)) {
                Row row = rows.get(0);
                return row.getString(CollUtil.getFirst(row.keySet()));
            }
        } catch (Exception e) {
            log.error("查询字典出错", e);
        }
        return null;
    }

    private String getCache(String key){
        if(redisUtil != null){
            if (redisUtil.hasKey(key)) {
                return redisUtil.get(key);
            }
        }else{
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
        }
        return null;
    }
    private String putCache(String key,String value,long time){
        if(Func.notNull(value)){
            if(redisUtil != null){
                redisUtil.set(key, value, time);
            }else{
                cache.put(key, value, time * 1000);
            }
        }
        return value;
    }
}
