package top.kthirty.core.secure.token;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
@Slf4j
class JwtUtil {
    private static final String SECRET = "O7hO6ooU8eyv98EPHgKmGNH6GXOSxV5V";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String getToken(Map<String,Object> claims,long expire){
        JwtBuilder jwtBuilder = Jwts.builder()
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .id(IdUtil.fastSimpleUUID())
                .expiration(Date.from(Instant.now().plusSeconds(expire)))
                .issuedAt(new Date())
                .issuer("KThirtySecure")
                .signWith(KEY, Jwts.SIG.HS256);
        claims.forEach(jwtBuilder::claim);
        return jwtBuilder.compact();
    }

    public static Claims getClaims(String token){
        try{
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            return null;
        }
    }
}
