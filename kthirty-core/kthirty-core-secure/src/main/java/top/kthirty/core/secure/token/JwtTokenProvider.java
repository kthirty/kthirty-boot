package top.kthirty.core.secure.token;

import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.config.KthirtySecureProperties;
import top.kthirty.core.tool.cache.Cache;
import top.kthirty.core.tool.exception.NotLoginException;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class JwtTokenProvider implements TokenProvider{
    private final KthirtySecureProperties secureProperties;
    private static final String USER_PREFIX = "auth:token:";

    interface ClaimKey{
        String TOKEN_TYPE = "token_type";
        String ACCESS_TOKEN = "AccessToken";
        String REFRESH_TOKEN = "RefreshToken";
        String USERNAME = "username";
    }


    @Override
    public TokenInfo auth(SysUser sysUser) {
        String username = sysUser.getUsername();
        Assert.hasText(username, "用来生成Token的用户登录名不可为空");
        TokenInfo tokenInfo = new TokenInfo();
        Map<String, Object> claim = new HashMap<>();
        tokenInfo.setUsername(username);
        tokenInfo.setUserId(sysUser.getId());
        claim.put(ClaimKey.USERNAME, username);
        claim.put(ClaimKey.TOKEN_TYPE, ClaimKey.ACCESS_TOKEN);
        // AccessToken
        String accessToken = JwtUtil.getToken(claim, secureProperties.getAccessTokenValidity());
        tokenInfo.setAccessToken(accessToken);
        // RefreshToken
        claim.put(ClaimKey.TOKEN_TYPE,  ClaimKey.REFRESH_TOKEN);
        String refreshToken = JwtUtil.getToken(claim, secureProperties.getRefreshTokenValidity());
        tokenInfo.setRefreshToken(refreshToken);
        tokenInfo.setExpiresIn(secureProperties.getAccessTokenValidity());
        tokenInfo.setRealName(sysUser.getRealName());
        tokenInfo.setRoles(sysUser.getRoles());
        // 存储AccessToken 对User缓存
        putCache(USER_PREFIX + ClaimKey.ACCESS_TOKEN + ":" + accessToken, sysUser,secureProperties.getAccessTokenValidity());
        // 存储RefreshToken 对User缓存
        putCache(USER_PREFIX + ClaimKey.REFRESH_TOKEN + ":" + refreshToken, sysUser, secureProperties.getRefreshTokenValidity());
        return tokenInfo;
    }


    @Override
    public TokenInfo refresh(String refreshToken) {
        try{
            SysUser sysUser = getCache(USER_PREFIX + ClaimKey.REFRESH_TOKEN + ":" + refreshToken);
            int accessTokenValidity = secureProperties.getAccessTokenValidity();
            String username = sysUser.getUsername();
            TokenInfo tokenInfo = new TokenInfo();
            Map<String, Object> claim = new HashMap<>();
            claim.put(ClaimKey.USERNAME, username);
            claim.put(ClaimKey.TOKEN_TYPE, ClaimKey.ACCESS_TOKEN);
            String accessToken = JwtUtil.getToken(claim,accessTokenValidity);
            tokenInfo.setAccessToken(accessToken);
            tokenInfo.setRefreshToken(refreshToken);
            tokenInfo.setExpiresIn(accessTokenValidity);
            putCache(USER_PREFIX + ClaimKey.ACCESS_TOKEN + ":" + accessToken, sysUser,accessTokenValidity);
            return tokenInfo;
        }catch (Exception e){
            throw new NotLoginException(e);
        }
    }

    @Override
    public SysUser getCurrentUser(String accessToken) {
        try{
            return getCache(USER_PREFIX + ClaimKey.ACCESS_TOKEN + ":" + accessToken);
        }catch (Exception e){
            return null;
        }
    }

    private void putCache(String key, SysUser sysUser, long seconds){
        Cache.add(key,sysUser,seconds);
    }
    private SysUser getCache(String key){
        return Cache.get(key);
    }


    @Override
    public void logout(String accessToken) {
        Cache.del(USER_PREFIX + ClaimKey.ACCESS_TOKEN + ":" + accessToken);
    }

}
