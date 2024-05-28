package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import top.kthirty.system.entity.Role;
import top.kthirty.system.service.RoleService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import top.kthirty.system.model.RoleConfigMenuVO;

import java.io.Serializable;
import java.util.List;

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
    public List<Role> list() {
        return roleService.list();
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

    @GetMapping("page")
    @Operation(summary = "分页查询角色",description="分页查询角色")
    public Page<Role> page(@Parameter(description="分页信息")Page<Role> page) {
        return roleService.page(page);
    }

}
