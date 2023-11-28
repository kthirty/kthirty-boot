package top.kthirty.core.secure.token;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.config.KthirtySecureProperties;

/**
 * <p>
 * 默认Token实现器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/27
 */

public class JwtCacheTokenProvider extends AbstractJwtTokenProvider {
    private static final Cache<String,SysUser> LOCAL_CACHE = CacheUtil.newLFUCache(10000);

    public JwtCacheTokenProvider(KthirtySecureProperties secureProperties) {
        super(secureProperties);
    }

    @Override
    protected void putCache(String key, SysUser sysUser, long seconds) {
        LOCAL_CACHE.put(key,sysUser,seconds * 1000L);
    }

    @Override
    protected SysUser getCache(String key) {
        return LOCAL_CACHE.get(key);
    }
}
