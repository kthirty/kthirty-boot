package top.kthirty.core.tool.cache;

import top.kthirty.core.tool.utils.SpringUtil;

/**
 * 缓存中转
 */
public class Cache {
    public static final CacheHandler CACHE_HANDLER = SpringUtil.getBean(CacheHandler.class);

    public static <T> void add(String key, T value, long second) {
        CACHE_HANDLER.add(key, value, second);
    }
    public static <T> void set(String key, T value, long second) {
        add(key, value, second);
    }
    public static <T> void set(String key, T value) {
        add(key, value, -1);
    }
    public static boolean hasKey(String key) {
        return has(key);
    }

    public static <T> T get(String key) {
        return CACHE_HANDLER.get(key);
    }

    public static <T> void del(String... key) {
        CACHE_HANDLER.del(key);
    }

    public static boolean has(String key) {
        return CACHE_HANDLER.has(key);
    }

    public static void clear() {
        CACHE_HANDLER.clear();
    }
}
