package top.kthirty.core.tool.cache;

import top.kthirty.core.tool.redis.RedisUtil;

import java.util.Set;

public class RedisCacheHandler implements CacheHandler {

    @Override
    public <T> void add(String key, T value, long seconds) {
        RedisUtil.set(key,value,seconds);
    }

    @Override
    public void del(String... keys) {
        RedisUtil.del(keys);
    }

    @Override
    public <T> T get(String key) {
        return RedisUtil.get(key);
    }

    @Override
    public boolean has(String key) {
        return RedisUtil.hasKey(key);
    }

    @Override
    public void clear() {
        RedisUtil.keys("*").forEach(this::del);
    }

    @Override
    public Set<String> keys(String pattern) {
        return RedisUtil.keys(pattern);
    }
}
