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
import top.kthirty.system.relation.entity.UserDeptRl;
import top.kthirty.system.relation.service.UserDeptRlService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户部门关联表 控制层。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@RestController
@Tag(name = "用户部门关联表接口")
@AllArgsConstructor
@RequestMapping("/userDeptRl")
public class UserDeptRlController extends BaseController {
    private final UserDeptRlService userDeptRlService;

    @PostMapping("save")
    @Operation(summary = "保存用户部门关联表",description="保存用户部门关联表")
    public boolean save(@RequestBody @Parameter(description="用户部门关联表")@Valid UserDeptRl userDeptRl) {
        return userDeptRlService.save(userDeptRl);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键用户部门关联表",description="根据主键用户部门关联表")
    public boolean remove(@PathVariable @Parameter(description="用户部门关联表主键")Serializable id) {
        return userDeptRlService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新用户部门关联表",description="根据主键更新用户部门关联表")
    public boolean update(@RequestBody @Parameter(description="用户部门关联表主键")@Valid UserDeptRl userDeptRl) {
        return userDeptRlService.updateById(userDeptRl);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有用户部门关联表",description="查询所有用户部门关联表")
    public List<UserDeptRl> list() {
        return userDeptRlService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取用户部门关联表",description="根据主键获取用户部门关联表")
    public UserDeptRl getInfo(@PathVariable Serializable id) {
        return userDeptRlService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户部门关联表",description="分页查询用户部门关联表")
    public Page<UserDeptRl> page(@Parameter(description="分页信息")Page<UserDeptRl> page) {
        return userDeptRlService.page(page);
    }

}
