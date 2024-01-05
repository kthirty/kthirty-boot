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
 * @author Thinkpad
 * @since 2024-01-05
 */
@RestController
@Tag(name = "菜单接口")
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController extends BaseController {
    private final MenuService menuService;

    @PostMapping("save")
    @Operation(summary = "保存菜单",description="保存菜单")
    public boolean save(@RequestBody @Parameter(description="菜单")@Valid Menu menu) {
        return menuService.save(menu);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键菜单",description="根据主键菜单")
    public boolean remove(@PathVariable @Parameter(description="菜单主键")Serializable id) {
        return menuService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新菜单",description="根据主键更新菜单")
    public boolean update(@RequestBody @Parameter(description="菜单主键")@Valid Menu menu) {
        return menuService.updateById(menu);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有菜单",description="查询所有菜单")
    public List<Menu> list() {
        return menuService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取菜单",description="根据主键获取菜单")
    public Menu getInfo(@PathVariable Serializable id) {
        return menuService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询菜单",description="分页查询菜单")
    public Page<Menu> page(@Parameter(description="分页信息")Page<Menu> page) {
        return menuService.page(page);
    }

}
