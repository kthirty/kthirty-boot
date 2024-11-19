package top.kthirty.system.service;

import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.UserRoleRl;

import java.util.List;

/**
 * 用户角色关联表 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface UserRoleRlService extends BaseService<UserRoleRl> {

    /***
     * 批量赋予角色
     * @author Kthirty
     * @since 2024/7/9
     * @param roleId 角色ID
     * @param userIds 用户id集合
     */
    void batchAdd(String roleId, List<String> userIds);
}
