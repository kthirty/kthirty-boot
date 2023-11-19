package top.kthirty.core.boot.secure;

public interface SysUserProvider {
    /**
     * 获取当前登录用户
     * @author Kthirty
     * @since 2023/11/19
     * @return top.kthirty.core.boot.secure.SysUser
     */
    SysUser getUser();
}
