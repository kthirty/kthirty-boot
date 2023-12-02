package top.kthirty.system.role.controller;

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
import top.kthirty.system.role.entity.Role;
import top.kthirty.system.role.service.RoleService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 角色 控制层。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@RestController
@Tag(name = "角色接口")
@AllArgsConstructor
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final RoleService roleService;

    /**
     * 添加角色。
     *
     * @param role 角色
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @Operation(summary = "保存角色",description="保存角色")
    public boolean save(@RequestBody @Parameter(description="角色")@Valid Role role) {
        return roleService.save(role);
    }

    /**
     * 根据主键删除角色。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键角色",description="根据主键角色")
    public boolean remove(@PathVariable @Parameter(description="角色主键")Serializable id) {
        return roleService.removeById(id);
    }

    /**
     * 根据主键更新角色。
     *
     * @param role 角色
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新角色",description="根据主键更新角色")
    public boolean update(@RequestBody @Parameter(description="角色主键")@Valid Role role) {
        return roleService.updateById(role);
    }

    /**
     * 查询所有角色。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(summary = "查询所有角色",description="查询所有角色")
    public List<Role> list() {
        return roleService.list();
    }

    /**
     * 根据角色主键获取详细信息。
     *
     * @param id 角色主键
     * @return 角色详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取角色",description="根据主键获取角色")
    public Role getInfo(@PathVariable Serializable id) {
        return roleService.getById(id);
    }

    /**
     * 分页查询角色。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询角色",description="分页查询角色")
    public Page<Role> page(@Parameter(description="分页信息")Page<Role> page) {
        return roleService.page(page);
    }

}
