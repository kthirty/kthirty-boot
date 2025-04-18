package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.tool.Func;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.entity.User;
import top.kthirty.system.entity.table.UserDeptRlTableDef;
import top.kthirty.system.entity.table.UserTableDef;
import top.kthirty.system.service.UserService;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息 控制层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@RestController
@Tag(name = "用户信息接口")
@AllArgsConstructor
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    private final UserService userService;

    @PostMapping("save")
    @Operation(summary = "保存用户信息",description="保存用户信息")
    public boolean save(@RequestBody @Parameter(description="用户信息")@Valid User user) {
        return userService.save(user);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键用户信息",description="根据主键用户信息")
    public boolean remove(@PathVariable @Parameter(description="用户信息主键")Serializable id) {
        return userService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新用户信息",description="根据主键更新用户信息")
    public boolean update(@RequestBody @Parameter(description="用户信息主键")@Valid User user) {
        return userService.updateById(user);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有用户信息",description="查询所有用户信息")
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取用户信息",description="根据主键获取用户信息")
    public User getInfo(@PathVariable Serializable id) {
        return userService.getMapper().selectOneWithRelationsById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户信息",description="分页查询用户信息")
    public Page<User> page(@Parameter(description="分页信息")Page<User> page,User user) {
        QueryWrapper wrapper = Condition.getWrapper(user);
        wrapper.leftJoin(UserDeptRlTableDef.USER_DEPT_RL).on(UserDeptRlTableDef.USER_DEPT_RL.USER_ID.eq(UserTableDef.USER.ID));
        wrapper.where(UserDeptRlTableDef.USER_DEPT_RL.DEPT_ID.in(user.getDeptIds(), Func.isNotEmpty(user.getDeptIds())));

        return userService.page(page, wrapper);
    }

    @PostMapping("saveUserAuth")
    @Operation(summary = "保存用户授权信息",description="保存用户授权信息")
    public void saveUserAuth(@RequestBody @Parameter(description="用户授权信息")User user) {
        userService.saveUserAuth(user);
    }

}
