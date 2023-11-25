package top.kthirty.core.boot.secure;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 默认用户提供器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/20
 */
@Slf4j
public class DefaultSysUserProvider implements SysUserProvider {
    public DefaultSysUserProvider(){
        log.warn("当前用户提供器未实现,这将导致框架内所有获取当前用户相关功能失效。请实现top.kthirty.core.boot.secure.SysUserProvider接口并注入Spring ");
    }
    @Override
    public SysUser getUser() {
        return null;
    }
}
