package top.kthirty.core.secure.token;

import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.config.KthirtySecureProperties;
import top.kthirty.core.tool.redis.RedisUtil;

/**
 * <p>
 * 默认Token实现器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/27
 */
public class JwtRedisTokenProvider extends AbstractJwtTokenProvider {

    public JwtRedisTokenProvider(KthirtySecureProperties secureProperties) {
        super(secureProperties);
    }


    @Override
    protected void putCache(String key, SysUser sysUser, long seconds) {
        RedisUtil.set(key,sysUser,seconds);
    }

    @Override
    protected SysUser getCache(String key) {
        return RedisUtil.get(key);
    }
}
