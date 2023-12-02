package top.kthirty.system.dept.controller;

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
import top.kthirty.system.dept.entity.Dept;
import top.kthirty.system.dept.service.DeptService;
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
 * @author KTHIRTY
 * @since 2023-12-02
 */
@RestController
@Tag(name = "部门信息接口")
@AllArgsConstructor
@RequestMapping("/dept")
public class DeptController extends BaseController {
    private final DeptService deptService;

    /**
     * 添加部门信息。
     *
     * @param dept 部门信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @Operation(summary = "保存部门信息",description="保存部门信息")
    public boolean save(@RequestBody @Parameter(description="部门信息")@Valid Dept dept) {
        return deptService.save(dept);
    }

    /**
     * 根据主键删除部门信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键部门信息",description="根据主键部门信息")
    public boolean remove(@PathVariable @Parameter(description="部门信息主键")Serializable id) {
        return deptService.removeById(id);
    }

    /**
     * 根据主键更新部门信息。
     *
     * @param dept 部门信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新部门信息",description="根据主键更新部门信息")
    public boolean update(@RequestBody @Parameter(description="部门信息主键")@Valid Dept dept) {
        return deptService.updateById(dept);
    }

    /**
     * 查询所有部门信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(summary = "查询所有部门信息",description="查询所有部门信息")
    public List<Dept> list() {
        return deptService.list();
    }

    /**
     * 根据部门信息主键获取详细信息。
     *
     * @param id 部门信息主键
     * @return 部门信息详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取部门信息",description="根据主键获取部门信息")
    public Dept getInfo(@PathVariable Serializable id) {
        return deptService.getById(id);
    }

    /**
     * 分页查询部门信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询部门信息",description="分页查询部门信息")
    public Page<Dept> page(@Parameter(description="分页信息")Page<Dept> page) {
        return deptService.page(page);
    }

}
