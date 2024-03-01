package top.kthirty.system.menu.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.menu.entity.Menu;
import top.kthirty.system.menu.service.MenuService;
import top.kthirty.system.role.entity.Role;

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
@Slf4j
public class MenuController extends BaseController {
    private final MenuService menuService;

    @PostMapping("save")
    @Operation(summary = "保存菜单", description = "保存菜单")
    public boolean save(@RequestBody @Parameter(description = "菜单") @Valid Menu menu) {
        return menuService.save(menu);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键菜单", description = "根据主键菜单")
    public boolean remove(@PathVariable @Parameter(description = "菜单主键") Serializable id) {
        return menuService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新菜单", description = "根据主键更新菜单")
    public boolean update(@RequestBody @Parameter(description = "菜单主键") @Valid Menu menu) {
        return menuService.updateById(menu);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有菜单", description = "查询所有菜单")
    public List<Menu> list() {
        return menuService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取菜单", description = "根据主键获取菜单")
    public Menu getInfo(@PathVariable Serializable id) {
        return menuService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询菜单", description = "分页查询菜单")
    public Page<Menu> page(Query<Menu> query, Menu menu) {
        return menuService.page(query.getPage(), Condition.getWrapper(menu));
    }

}
