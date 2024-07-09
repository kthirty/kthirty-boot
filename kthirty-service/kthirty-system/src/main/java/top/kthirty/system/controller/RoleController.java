package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.entity.Role;
import top.kthirty.system.entity.User;
import top.kthirty.system.entity.UserRoleRl;
import top.kthirty.system.model.AddUserRoleLinkReq;
import top.kthirty.system.model.RoleConfigMenuVO;
import top.kthirty.system.service.RoleService;
import top.kthirty.system.service.UserRoleRlService;

import java.io.Serializable;
import java.util.List;

import static top.kthirty.system.entity.table.UserRoleRlTableDef.USER_ROLE_RL;

/**
 * 角色 控制层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@RestController
@Tag(name = "角色接口")
@AllArgsConstructor
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
    private final RoleService roleService;
    private final UserRoleRlService userRoleRlService;

    @PostMapping("save")
    @Operation(summary = "保存角色",description="保存角色")
    public boolean save(@RequestBody @Parameter(description="角色")@Valid Role role) {
        return roleService.save(role);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键角色",description="根据主键角色")
    public boolean remove(@PathVariable @Parameter(description="角色主键")Serializable id) {
        return roleService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新角色",description="根据主键更新角色")
    public boolean update(@RequestBody @Parameter(description="角色主键")@Valid Role role) {
        return roleService.updateById(role);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有角色",description="查询所有角色")
    public List<Role> list(Role role) {
        return roleService.list(Condition.getWrapper(role));
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取角色",description="根据主键获取角色")
    public Role getInfo(@PathVariable Serializable id) {
        return roleService.getById(id);
    }

    @GetMapping("menus/{id}")
    @Operation(summary = "根据主键获取角色拥有的菜单",description="根据主键获取角色拥有的菜单")
    public List<String> queryMenus(@PathVariable String id){
        return roleService.queryMenus(id);
    }
    @PostMapping("configMenus")
    @Operation(summary = "保存角色拥有的菜单",description="保存角色拥有的菜单")
    public void saveMenus(@RequestBody @Valid RoleConfigMenuVO req){
        roleService.saveMenus(req);
    }

    @GetMapping("users/{id}")
    @Operation(summary = "根据主键获取此角色的用户",description="根据主键获取此角色的用户")
    public List<User> queryUsers(@PathVariable String id){
        return roleService.queryUsers(id);
    }
    @DeleteMapping("removeLink")
    @Operation(summary = "根据主键获取此角色的用户",description="根据主键获取此角色的用户")
    public void removeLink(@RequestBody @Valid UserRoleRl req) {
        userRoleRlService.remove(USER_ROLE_RL.USER_ID.eq(req.getUserId()).and(USER_ROLE_RL.ROLE_ID.eq(req.getRoleId())));
    }
    @PostMapping("addLink")
    @Operation(summary = "根据主键获取此角色的用户",description="根据主键获取此角色的用户")
    public void addLink(@RequestBody @Valid AddUserRoleLinkReq req){
        userRoleRlService.batchAdd(req.getRoleId(),req.getUserIds());
    }

    @GetMapping("page")
    @Operation(summary = "分页查询角色",description="分页查询角色")
    public Page<Role> page(@Parameter(description="分页信息")Page<Role> page,Role role) {
        return roleService.page(page,Condition.getWrapper(role));
    }

}
