package top.kthirty.system.relation.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import top.kthirty.system.relation.entity.RoleMenuRl;
import top.kthirty.system.relation.service.RoleMenuRlService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单关联表 控制层。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@RestController
@Tag(name = "角色菜单关联表接口")
@AllArgsConstructor
@RequestMapping("/roleMenuRl")
public class RoleMenuRlController extends BaseController {
    private final RoleMenuRlService roleMenuRlService;

    @PostMapping("save")
    @Operation(summary = "保存角色菜单关联表",description="保存角色菜单关联表")
    public boolean save(@RequestBody @Parameter(description="角色菜单关联表")@Valid RoleMenuRl roleMenuRl) {
        return roleMenuRlService.save(roleMenuRl);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键角色菜单关联表",description="根据主键角色菜单关联表")
    public boolean remove(@PathVariable @Parameter(description="角色菜单关联表主键")Serializable id) {
        return roleMenuRlService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新角色菜单关联表",description="根据主键更新角色菜单关联表")
    public boolean update(@RequestBody @Parameter(description="角色菜单关联表主键")@Valid RoleMenuRl roleMenuRl) {
        return roleMenuRlService.updateById(roleMenuRl);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有角色菜单关联表",description="查询所有角色菜单关联表")
    public List<RoleMenuRl> list() {
        return roleMenuRlService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取角色菜单关联表",description="根据主键获取角色菜单关联表")
    public RoleMenuRl getInfo(@PathVariable Serializable id) {
        return roleMenuRlService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询角色菜单关联表",description="分页查询角色菜单关联表")
    public Page<RoleMenuRl> page(@Parameter(description="分页信息")Page<RoleMenuRl> page) {
        return roleMenuRlService.page(page);
    }

}
