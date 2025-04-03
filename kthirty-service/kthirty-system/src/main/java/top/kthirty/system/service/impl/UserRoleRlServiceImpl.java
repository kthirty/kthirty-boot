package top.kthirty.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.system.entity.UserRoleRl;
import top.kthirty.system.mapper.UserRoleRlMapper;
import top.kthirty.system.service.UserRoleRlService;

import java.util.List;

import static top.kthirty.system.entity.table.UserRoleRlTableDef.USER_ROLE_RL;

/**
 * 用户角色关联表 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class UserRoleRlServiceImpl extends ServiceImpl<UserRoleRlMapper, UserRoleRl> implements UserRoleRlService {

    @Override
    public void batchAdd(String roleId, List<String> userIds) {
        List<String> alreadyHasUserIds = list(USER_ROLE_RL.ROLE_ID.eq(roleId)).stream().map(UserRoleRl::getUserId).toList();
        if (CollUtil.isNotEmpty(alreadyHasUserIds)) {
            userIds.removeAll(alreadyHasUserIds);
        }
        if(CollUtil.isNotEmpty(userIds)){
            List<UserRoleRl> roleRls = userIds.stream().map(userId -> new UserRoleRl().setRoleId(roleId).setUserId(userId)).toList();
            saveBatch(roleRls);
        }
    }
}
