package top.kthirty.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import top.kthirty.system.entity.Dept;
import top.kthirty.system.service.DeptService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 部门信息 控制层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@RestController
@Tag(name = "部门信息接口")
@AllArgsConstructor
@RequestMapping("/sys/dept")
public class DeptController extends BaseController {
    private final DeptService deptService;

    @PostMapping("save")
    @Operation(summary = "保存部门信息",description="保存部门信息")
    public boolean save(@RequestBody @Parameter(description="部门信息")@Valid Dept dept) {
        return deptService.save(dept);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键部门信息",description="根据主键部门信息")
    public boolean remove(@PathVariable @Parameter(description="部门信息主键")Serializable id) {
        return deptService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新部门信息",description="根据主键更新部门信息")
    public boolean update(@RequestBody @Parameter(description="部门信息主键")@Valid Dept dept) {
        return deptService.updateById(dept);
    }

    @GetMapping("tree")
    @Operation(summary = "查询所有部门信息树",description="查询所有部门信息树")
    public List<Tree<String>> tree() {
        return deptService.tree();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取部门信息",description="根据主键获取部门信息")
    public Dept getInfo(@PathVariable Serializable id) {
        return deptService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询部门信息",description="分页查询部门信息")
    public Page<Dept> page(@Parameter(description="分页信息")Page<Dept> page) {
        return deptService.page(page);
    }

}
