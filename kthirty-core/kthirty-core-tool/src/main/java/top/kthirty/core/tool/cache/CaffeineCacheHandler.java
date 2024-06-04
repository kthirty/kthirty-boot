package top.kthirty.core.tool.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;

/**
 * 本地缓存
 */
@SuppressWarnings("unchecked")
public class LocalCacheHandler implements CacheHandler{
    private static final Cache<String,Object> LOCAL_CACHE = CacheUtil.newLFUCache(10000);
    @Override
    public <T> void add(String key, T value, long second) {
        LOCAL_CACHE.put(key, value,second*1000);
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            LOCAL_CACHE.remove(key);
        }
    }

    @Override
    public <T> T get(String key) {
        return (T)LOCAL_CACHE.get(key);
    }

    @Override
    public boolean has(String key) {
        return LOCAL_CACHE.containsKey(key);
    }

    @Override
    public void clear() {
        LOCAL_CACHE.clear();
    }
}
