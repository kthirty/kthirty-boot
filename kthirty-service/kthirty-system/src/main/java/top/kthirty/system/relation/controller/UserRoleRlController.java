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
import top.kthirty.system.relation.entity.UserRoleRl;
import top.kthirty.system.relation.service.UserRoleRlService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户角色关联表 控制层。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@RestController
@Tag(name = "用户角色关联表接口")
@AllArgsConstructor
@RequestMapping("/userRoleRl")
public class UserRoleRlController extends BaseController {
    private final UserRoleRlService userRoleRlService;

    @PostMapping("save")
    @Operation(summary = "保存用户角色关联表",description="保存用户角色关联表")
    public boolean save(@RequestBody @Parameter(description="用户角色关联表")@Valid UserRoleRl userRoleRl) {
        return userRoleRlService.save(userRoleRl);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键用户角色关联表",description="根据主键用户角色关联表")
    public boolean remove(@PathVariable @Parameter(description="用户角色关联表主键")Serializable id) {
        return userRoleRlService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新用户角色关联表",description="根据主键更新用户角色关联表")
    public boolean update(@RequestBody @Parameter(description="用户角色关联表主键")@Valid UserRoleRl userRoleRl) {
        return userRoleRlService.updateById(userRoleRl);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有用户角色关联表",description="查询所有用户角色关联表")
    public List<UserRoleRl> list() {
        return userRoleRlService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取用户角色关联表",description="根据主键获取用户角色关联表")
    public UserRoleRl getInfo(@PathVariable Serializable id) {
        return userRoleRlService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户角色关联表",description="分页查询用户角色关联表")
    public Page<UserRoleRl> page(@Parameter(description="分页信息")Page<UserRoleRl> page) {
        return userRoleRlService.page(page);
    }

}
