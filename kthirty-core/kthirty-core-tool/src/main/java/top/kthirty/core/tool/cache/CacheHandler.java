package top.kthirty.core.tool.cache;

import java.util.Set;

/**
 * 缓存处理器
 */
public interface CacheHandler {
    /**
     * 添加缓存
     */
    <T> void add(String key, T value,long second);

    /**
     * 删除缓存
     */
    void del(String... key);

    /**
     * 获取缓存
     */
    <T> T get(String key);

    /**
     * 判断是否包含缓存
     */
    boolean has(String key);

    /**
     * 删除所有缓存
     */
    void clear();

    Set<String> keys(String pattern);
}
