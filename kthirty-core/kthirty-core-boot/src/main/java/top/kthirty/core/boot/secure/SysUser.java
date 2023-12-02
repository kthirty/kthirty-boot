package top.kthirty.core.boot.secure;

import cn.hutool.core.lang.Dict;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
/**
 * <p>
 * 当前登录用户
 * </p>
 *
 * @author KThirty
 * @since 2023/11/20
 */
@Data
@Accessors(chain = true)
public class SysUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String id;
    /**
     * 登录名
     */
    private String username;
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
    /**
     * 额外拓展
     */
    private Dict extra;
}
