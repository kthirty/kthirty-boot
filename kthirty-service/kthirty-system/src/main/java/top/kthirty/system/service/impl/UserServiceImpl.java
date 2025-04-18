package top.kthirty.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.db.base.service.BaseServiceImpl;
import top.kthirty.system.entity.User;
import top.kthirty.system.entity.UserDeptRl;
import top.kthirty.system.entity.UserRoleRl;
import top.kthirty.system.entity.table.UserDeptRlTableDef;
import top.kthirty.system.entity.table.UserRoleRlTableDef;
import top.kthirty.system.mapper.UserMapper;
import top.kthirty.system.service.UserDeptRlService;
import top.kthirty.system.service.UserRoleRlService;
import top.kthirty.system.service.UserService;

import java.util.List;

/**
 * 用户信息 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    private final UserRoleRlService userRoleRlService;
    private final UserDeptRlService userDeptRlService;

    @Override
    public <R> Page<R> pageAs(Page<R> page, QueryWrapper query, Class<R> asType) {
        return getMapper()
                .paginateWithRelationsAs(page, query, asType);
    }

    @Override
    public boolean save(User entity) {
        boolean userSave = super.save(entity);
        Assert.isTrue(userSave, "save user error");
        this.saveUserRl(entity, false);
        return true;
    }

    /**
     * 保存用户关系表
     *
     * @param entity  用户实体
     * @param clearDb 是否清空数据库中历史
     * @author Kthirty
     * @since 2024/7/7
     */
    private void saveUserRl(User entity, boolean clearDb) {
        // 删除历史
        if (clearDb) {
            userRoleRlService.remove(UserRoleRlTableDef.USER_ROLE_RL.USER_ID.eq(entity.getId()));
            userDeptRlService.remove(UserDeptRlTableDef.USER_DEPT_RL.USER_ID.eq(entity.getId()));
        }
        // 保存角色关系
        List<String> roleIds = ObjUtil.defaultIfNull(entity.getRoleIds(), ListUtil.empty());
        if (CollUtil.isNotEmpty(roleIds)) {
            List<UserRoleRl> roleRls = roleIds.stream().map(roleId -> new UserRoleRl().setRoleId(roleId).setUserId(entity.getId())).toList();
            userRoleRlService.saveBatch(roleRls);
        }
        // 保存部门关系
        List<String> deptIds = ObjUtil.defaultIfNull(entity.getDeptIds(), ListUtil.empty());
        if (CollUtil.isNotEmpty(deptIds)) {
            List<UserDeptRl> userDeptRls = deptIds.stream().map(deptId -> new UserDeptRl().setUserId(entity.getId()).setDeptId(deptId)).toList();
            userDeptRlService.saveBatch(userDeptRls);
        }
    }

    @Override
    public boolean updateById(User entity) {
        boolean updateUser = super.updateById(entity);
        Assert.isTrue(updateUser, "update user error");
        this.saveUserRl(entity, true);
        return true;
    }

    @Override
    public void saveUserAuth(User user) {
        userRoleRlService.remove(QueryWrapper.create().and(UserRoleRlTableDef.USER_ROLE_RL.USER_ID.eq(user.getId())));
        userDeptRlService.remove(QueryWrapper.create().and(UserDeptRlTableDef.USER_DEPT_RL.USER_ID.eq(user.getId())));
        if (CollUtil.isNotEmpty(user.getRoleRls())) {
            List<UserRoleRl> userRoleRls = user.getRoleRls().stream().map(it -> it.setUserId(user.getId())).toList();
            userRoleRlService.saveBatch(userRoleRls);
        }
        if(CollUtil.isNotEmpty(user.getDeptRls())){
            List<UserDeptRl> userDeptRls = user.getDeptRls().stream().map(it -> it.setUserId(user.getId())).toList();
            userDeptRlService.saveBatch(userDeptRls);
        }
    }
}
