package top.kthirty.system.controller;

import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.annotation.IgnoreSecure;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.model.AuthParam;
import top.kthirty.system.service.AuthService;
import top.kthirty.system.util.CaptchaHelper;

import java.util.List;

@RestController
@Tag(name = "认证接口")
@AllArgsConstructor
@RequestMapping("/sys/auth")
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("login")
    @IgnoreSecure
    @Operation(summary = "获取认证token",description = "获取认证token")
    public TokenInfo token(@RequestBody @Valid AuthParam authParam){
        return authService.token(authParam);
    }


    @GetMapping("permCode")
    @IgnoreSecure
    @Operation(summary = "获取权限列表",description = "获取权限列表")
    public List<String> permCode(){
        return SecureUtil.getPermissions();
    }

    @GetMapping("logout")
    @IgnoreSecure
    @Operation(summary = "退出登录",description = "退出登录")
    public void logout(){}

    @Operation(summary = "获取验证码")
    @GetMapping("code")
    @IgnoreSecure
    public String code(){
        return CaptchaHelper.generateCode();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("info")
    public SysUser info(){
        return SecureUtil.getCurrentUser();
    }

    @Operation(summary = "获取当前用户的菜单列表")
    @GetMapping("menus")
    public  List<Tree<String>> menus(){
        return authService.menus();
    }

}
