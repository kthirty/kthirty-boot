package top.kthirty.core.secure.token;

import top.kthirty.core.boot.secure.SysUser;

/**
 * <p>
 * Token提供器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/27
 */
public interface TokenProvider {
    /**
     * 初次授权
     * @param sysUser 用户信息
     * @return TokenInfo
     */
    TokenInfo auth(SysUser sysUser);

    /**
     * 刷新Token
     * @param refreshToken 刷新Token
     * @return TokenInfo
     */
    TokenInfo refresh(String refreshToken);

    /**
     * 根据Token获取当前用户
     * @param accessToken token
     * @return 当前用户信息
     */
    SysUser getCurrentUser(String accessToken);
}
