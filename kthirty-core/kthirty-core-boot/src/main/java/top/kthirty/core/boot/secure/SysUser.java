package top.kthirty.core.boot.secure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SysUser {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 所属机构代码
     */
    private String orgCode;
    /**
     * 角色
     */
    private List<String> roles;
    /**
     * 权限集合
     */
    private List<String> permissions;
    /**
     * 租户ID
     * 如果管理多个租户可以返回多个，第一个为用户操作时的租户id
     */
    private List<String> tenantIds;
}
