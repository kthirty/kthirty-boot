package top.kthirty.core.secure.token;

import org.springframework.util.Assert;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.tool.utils.SpringUtil;
/**
 * <p>
 * Token工具类，逻辑由TokenProvider内部实现
 * @see TokenProvider
 * </p>
 *
 * @author KThirty
 * @since 2023/11/27
 */
public class TokenUtil {
    private static final TokenProvider PROVIDER = SpringUtil.getBeanSafe(TokenProvider.class);

    public static TokenInfo authorize(SysUser sysUser){
        Assert.notNull(PROVIDER,"TokenProvider不存在，请实现TokenProvider并注入Spring");
        return PROVIDER.auth(sysUser);
    }

    public static TokenInfo refresh(String refreshToken){
        Assert.notNull(PROVIDER,"TokenProvider不存在，请实现TokenProvider并注入Spring");
        return PROVIDER.refresh(refreshToken);
    }



}
