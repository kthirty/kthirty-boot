package top.kthirty.core.tool.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import cn.hutool.core.util.StrUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 本地缓存
 */
@SuppressWarnings("unchecked")
public class RuntimeCacheHandler implements CacheHandler {
    private static final LFUCache<String, Object> LOCAL_CACHE = CacheUtil.newLFUCache(10000);

    @Override
    public <T> void add(String key, T value, long second) {
        LOCAL_CACHE.put(key, value, second * 1000);
    }

    @Override
    public void del(String... keys) {
        for (String key : keys) {
            LOCAL_CACHE.remove(key);
        }
    }

    @Override
    public <T> T get(String key) {
        return (T) LOCAL_CACHE.get(key);
    }

    @Override
    public boolean has(String key) {
        return LOCAL_CACHE.containsKey(key);
    }

    @Override
    public void clear() {
        LOCAL_CACHE.clear();
    }

    @Override
    public Set<String> keys(String pattern) {
        return LOCAL_CACHE.keySet().stream().filter(it -> {
            if (StrUtil.equals(pattern, StringPool.ASTERISK)) {
                return true;
            } else if (StrUtil.startWith(pattern, StringPool.ASTERISK) && StrUtil.endWith(pattern, StringPool.ASTERISK)) {
                return StrUtil.contains(it, pattern.substring(1, pattern.length() - 1));
            } else if (StrUtil.startWith(pattern, StringPool.ASTERISK)) {
                return StrUtil.endWith(it, pattern.substring(1));
            } else if (StrUtil.endWith(pattern, StringPool.ASTERISK)) {
                return StrUtil.startWith(it, pattern.substring(0, pattern.length() - 1));
            } else {
                return StrUtil.equals(it, pattern);
            }
        }).collect(Collectors.toSet());
    }
}
