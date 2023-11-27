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
    private final RedisUtil redisUtil;

    public JwtRedisTokenProvider(RedisUtil redisUtil, KthirtySecureProperties secureProperties) {
        super(secureProperties);
        this.redisUtil = redisUtil;
    }


    @Override
    protected void putCache(String key, SysUser sysUser, long seconds) {
        redisUtil.set(key,sysUser,seconds);
    }

    @Override
    protected SysUser getCache(String key) {
        return redisUtil.get(key);
    }
}
