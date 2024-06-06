package top.kthirty.core.secure.token;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.config.KthirtySecureProperties;
import top.kthirty.core.tool.exception.NotLoginException;
import top.kthirty.core.tool.cache.Cache;

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

        // 存储username 对User缓存,时长使用Max(AccessTokenValidity,RefreshTokenValidity)
        int maxSecond = Math.max(secureProperties.getAccessTokenValidity(), secureProperties.getRefreshTokenValidity());
        putCache(USER_PREFIX + username, sysUser,maxSecond);
        return tokenInfo;
    }


    @Override
    public TokenInfo refresh(String refreshToken) {
        try{
            String username = JwtUtil.getClaims(refreshToken).get("username", String.class);
            TokenInfo tokenInfo = new TokenInfo();
            Map<String, Object> claim = new HashMap<>();
            claim.put(ClaimKey.USERNAME, username);
            claim.put(ClaimKey.TOKEN_TYPE, ClaimKey.ACCESS_TOKEN);
            String accessToken = JwtUtil.getToken(claim,secureProperties.getAccessTokenValidity());
            tokenInfo.setAccessToken(accessToken);
            tokenInfo.setRefreshToken(refreshToken);
            tokenInfo.setExpiresIn(secureProperties.getAccessTokenValidity());
            return tokenInfo;
        }catch (Exception e){
            throw new NotLoginException(e);
        }
    }

    @Override
    public SysUser getCurrentUser(String accessToken) {
        try{
            Claims claims = JwtUtil.getClaims(accessToken);
            String username = claims.get(ClaimKey.USERNAME, String.class);
            return getCache(USER_PREFIX + username);
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

}
