package top.kthirty.system.auth.service.impl;

import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.core.secure.token.TokenUtil;
import top.kthirty.core.secure.util.PasswordUtil;
import top.kthirty.system.auth.model.AuthParam;
import top.kthirty.system.auth.service.AuthService;
import top.kthirty.system.menu.entity.Menu;
import top.kthirty.system.menu.service.MenuService;
import top.kthirty.system.relation.entity.UserRoleRl;
import top.kthirty.system.relation.service.UserRoleRlService;
import top.kthirty.system.role.service.RoleService;
import top.kthirty.system.user.entity.User;
import top.kthirty.system.user.service.UserService;

import java.util.List;

import static top.kthirty.system.menu.entity.table.MenuTableDef.MENU;
import static top.kthirty.system.relation.entity.table.RoleMenuRlTableDef.ROLE_MENU_RL;
import static top.kthirty.system.relation.entity.table.UserRoleRlTableDef.USER_ROLE_RL;
import static top.kthirty.system.role.entity.table.RoleTableDef.ROLE;
import static top.kthirty.system.user.entity.table.UserTableDef.USER;

/**
 * <p>
 * 认证相关Service
 * </p>
 *
 * @author KThirty
 * @since 2023/12/2
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final UserRoleRlService userRoleRlService;


    @Override
    public TokenInfo token(AuthParam authParam) {
        User user = userService.queryChain()
                .where(USER.USERNAME.eq(authParam.getUsername()))
                .and(USER.PASSWORD.eq(PasswordUtil.encrypt(authParam.getPassword())))
                .limit(1)
                .one();
        Assert.notNull(user, "用户名或密码错误");

        // 获取角色
        List<String> roles = userRoleRlService.queryChain().where(USER_ROLE_RL.USER_ID.eq(user.getId())).list()
                .stream()
                .map(UserRoleRl::getRoleCode)
                .toList();
        // 获取权限
        List<String> permissions = menuService.queryChain()
                .select(MENU.PERMISSION)
                .join(ROLE_MENU_RL).on(ROLE_MENU_RL.MENU_ID.eq(MENU.ID))
                .join(ROLE).on(ROLE_MENU_RL.ROLE_CODE.eq(ROLE.CODE))
                .where(MENU.PERMISSION.isNotNull())
                .and(ROLE.CODE.in(roles))
                .list()
                .stream()
                .map(Menu::getPermission)
                .toList();

        SysUser sysUser = new SysUser()
                .setUsername(user.getUsername())
                .setId(user.getId())
                .setRoles(roles)
                .setRealName(user.getRealName())
                .setPermissions(permissions);
        TokenInfo tokenInfo = TokenUtil.authorize(sysUser);
        return tokenInfo;
    }

    @Override
    public List<Menu> menus() {
        List<String> roles = SecureUtil.getRoles();
        return menuService.queryChain()
                .select(MENU.ALL_COLUMNS)
                .join(ROLE_MENU_RL).on(ROLE_MENU_RL.MENU_ID.eq(MENU.ID)).and(MENU.DELETED.eq("0"))
                .join(ROLE).on(ROLE_MENU_RL.ROLE_CODE.eq(ROLE.CODE)).and(ROLE.DELETED.eq("0"))
                .where(MENU.DELETED.eq("0"))
                .and(ROLE.CODE.in(roles))
                .and(MENU.STATUS.eq("1"))
                .list();
    }
}
