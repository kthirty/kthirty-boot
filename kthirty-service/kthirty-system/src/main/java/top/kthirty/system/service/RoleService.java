package top.kthirty.system.service;

import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.Role;
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

    void saveMenus(RoleConfigMenuVO req);
}
