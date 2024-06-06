package top.kthirty.core.tool.cache;


import java.util.Set;

public class RuntimeCache {
    public static final CacheHandler CACHE_HANDLER = new RuntimeCacheHandler();

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

    public static Set<String> keys(String pattern) {
        return CACHE_HANDLER.keys(pattern);
    }
}