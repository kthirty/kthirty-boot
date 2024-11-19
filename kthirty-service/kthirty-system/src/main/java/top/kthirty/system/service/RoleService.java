package top.kthirty.system.service;

import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.Role;
import top.kthirty.system.entity.User;
import top.kthirty.system.model.RoleConfigMenuVO;

import java.util.List;

/**
 * 角色 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface RoleService extends BaseService<Role> {

    List<String> queryMenus(String id);
    /**
     * 保存关联的菜单
     * @author Kthirty
     * @since 2024/7/8
     */
    void saveMenus(RoleConfigMenuVO req);
    /**
     * 查询拥有此权限的用户
     * @author Kthirty
     * @since 2024/7/8
     * @param id 角色ID
     * @return java.util.List<top.kthirty.system.entity.User>
     */
    List<User> queryUsers(String id);
}
