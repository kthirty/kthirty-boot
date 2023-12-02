package top.kthirty.system.menu.controller;

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
import top.kthirty.system.menu.entity.Menu;
import top.kthirty.system.menu.service.MenuService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单 控制层。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@RestController
@Tag(name = "菜单接口")
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController extends BaseController {
    private final MenuService menuService;

    /**
     * 添加菜单。
     *
     * @param menu 菜单
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @Operation(summary = "保存菜单",description="保存菜单")
    public boolean save(@RequestBody @Parameter(description="菜单")@Valid Menu menu) {
        return menuService.save(menu);
    }

    /**
     * 根据主键删除菜单。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键菜单",description="根据主键菜单")
    public boolean remove(@PathVariable @Parameter(description="菜单主键")Serializable id) {
        return menuService.removeById(id);
    }

    /**
     * 根据主键更新菜单。
     *
     * @param menu 菜单
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新菜单",description="根据主键更新菜单")
    public boolean update(@RequestBody @Parameter(description="菜单主键")@Valid Menu menu) {
        return menuService.updateById(menu);
    }

    /**
     * 查询所有菜单。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(summary = "查询所有菜单",description="查询所有菜单")
    public List<Menu> list() {
        return menuService.list();
    }

    /**
     * 根据菜单主键获取详细信息。
     *
     * @param id 菜单主键
     * @return 菜单详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取菜单",description="根据主键获取菜单")
    public Menu getInfo(@PathVariable Serializable id) {
        return menuService.getById(id);
    }

    /**
     * 分页查询菜单。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询菜单",description="分页查询菜单")
    public Page<Menu> page(@Parameter(description="分页信息")Page<Menu> page) {
        return menuService.page(page);
    }

}
