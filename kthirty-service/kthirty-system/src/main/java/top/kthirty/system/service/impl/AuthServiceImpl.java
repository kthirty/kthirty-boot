package top.kthirty.system.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.exception.NotLoginException;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.core.secure.token.TokenUtil;
import top.kthirty.core.secure.util.PasswordUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.system.entity.Menu;
import top.kthirty.system.entity.Role;
import top.kthirty.system.entity.User;
import top.kthirty.system.model.AuthParam;
import top.kthirty.system.service.AuthService;
import top.kthirty.system.service.MenuService;
import top.kthirty.system.service.UserRoleRlService;
import top.kthirty.system.service.UserService;

import java.util.List;

import static top.kthirty.system.entity.table.MenuTableDef.MENU;
import static top.kthirty.system.entity.table.RoleMenuRlTableDef.ROLE_MENU_RL;
import static top.kthirty.system.entity.table.RoleTableDef.ROLE;
import static top.kthirty.system.entity.table.UserRoleRlTableDef.USER_ROLE_RL;
import static top.kthirty.system.entity.table.UserTableDef.USER;

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
    private final MenuService menuService;
    private final UserRoleRlService userRoleRlService;


    @Override
    public TokenInfo token(AuthParam authParam) {
//        Assert.isTrue(CaptchaHelper.validateCode(authParam.getCode()),"验证码错误");
        User user = userService.queryChain()
                .where(USER.USERNAME.eq(authParam.getUsername()))
                .and(USER.PASSWORD.eq(PasswordUtil.encrypt(authParam.getPassword())))
                .limit(1)
                .one();
        Assert.notNull(user, "用户名或密码错误");

        // 获取角色
        List<String> roles = userRoleRlService.queryChain()
                .select(ROLE.ALL_COLUMNS)
                .join(ROLE).on(ROLE.ID.eq(USER_ROLE_RL.ROLE_ID))
                .where(USER_ROLE_RL.USER_ID.eq(user.getId())).listAs(Role.class)
                .stream()
                .map(Role::getCode)
                .toList();
        // 获取权限
        List<String> permissions = menuService.queryChain()
                .select(MENU.PERMISSION)
                .join(ROLE_MENU_RL).on(ROLE_MENU_RL.MENU_ID.eq(MENU.ID))
                .join(ROLE).on(ROLE_MENU_RL.ROLE_ID.eq(ROLE.ID))
                .where(MENU.PERMISSION.isNotNull())
                .and(MENU.PERMISSION.ne(StringPool.EMPTY))
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
    public List<Tree<String>> menus() {
        List<String> roles = SecureUtil.getRoles();
        Assert.notEmpty(roles,() -> new NotLoginException("当前用户尚未分配权限"));
        List<TreeNode<String>> list = menuService.queryChain()
                .select(MENU.ALL_COLUMNS)
                .join(ROLE_MENU_RL).on(ROLE_MENU_RL.MENU_ID.eq(MENU.ID)).and(MENU.DELETED.eq("0"))
                .join(ROLE).on(ROLE_MENU_RL.ROLE_ID.eq(ROLE.ID)).and(ROLE.DELETED.eq("0"))
                .where(MENU.DELETED.eq("0"))
                .and(ROLE.CODE.in(roles))
                .and(MENU.STATUS.eq("1"))
                .list()
                .stream().map(it -> {
                    TreeNode<String> treeNode = new TreeNode<>();
                    treeNode.setParentId(it.getParentId());
                    treeNode.setId(it.getId());
                    treeNode.setName(it.getComponentName());
                    treeNode.setWeight(Func.toInt(it.getSort(), 0));
                    Kv extra = Kv.init().set("meta", Kv.init()
                                    .set("title", it.getName())
                                    .set("hideMenu", !BooleanUtil.toBoolean(it.getShow()))
                                    .set("icon", it.getIcon()))
                            .set("path",it.getPath())
                            .set("component",it.getComponent());
                    treeNode.setExtra(extra);
                    return treeNode;
                }).toList();
        return TreeUtil.build(list, "0");
    }
}
