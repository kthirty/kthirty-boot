package top.kthirty.system.user.controller;

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
import top.kthirty.system.user.entity.User;
import top.kthirty.system.user.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 用户信息 控制层。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@RestController
@Tag(name = "用户信息接口")
@AllArgsConstructor
@RequestMapping("/user")
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
        return userService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户信息",description="分页查询用户信息")
    public Page<User> page(@Parameter(description="分页信息")Page<User> page) {
        return userService.page(page);
    }

}
