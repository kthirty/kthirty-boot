package top.kthirty.system.role.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kthirty.core.tool.Func;
import top.kthirty.system.relation.entity.RoleMenuRl;
import top.kthirty.system.relation.service.RoleMenuRlService;
import top.kthirty.system.role.entity.Role;
import top.kthirty.system.role.mapper.RoleMapper;
import top.kthirty.system.role.service.RoleService;
import top.kthirty.system.role.vo.RoleConfigMenuVO;

import java.util.List;

import static top.kthirty.system.relation.entity.table.RoleMenuRlTableDef.ROLE_MENU_RL;
import static top.kthirty.system.role.entity.table.RoleTableDef.ROLE;

/**
 * 角色 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
@AllArgsConstructor
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    private final RoleMenuRlService roleMenuRlService;

    @Override
    public List<String> queryMenus(String id) {
        return queryChain()
                .select(ROLE_MENU_RL.ALL_COLUMNS)
                .join(RoleMenuRl.class).on(ROLE_MENU_RL.ROLE_CODE.eq(ROLE.CODE))
                .where(ROLE.ID.eq(id))
                .listAs(RoleMenuRl.class)
                .stream()
                .map(RoleMenuRl::getMenuId)
                .toList();
    }

    @Override
    public void saveMenus(RoleConfigMenuVO req) {
        TimeInterval timer = DateUtil.timer();
        timer.start();
        Role role = getById(req.getRoleId());
        Assert.notNull(role,"角色不存在");
        roleMenuRlService.remove(ROLE_MENU_RL.ROLE_CODE.eq(role.getCode()));
        List<RoleMenuRl> roleMenuRls = req.getMenus().stream().map(it -> RoleMenuRl.builder().menuId(it).roleCode(role.getCode()).build()).toList();
        if(Func.isNotEmpty(roleMenuRls)){
             roleMenuRlService.saveBatch(roleMenuRls);
        }
    }
}
