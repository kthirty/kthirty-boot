package top.kthirty.system.auth.controller;

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
import top.kthirty.system.auth.model.AuthParam;
import top.kthirty.system.auth.service.AuthService;
import top.kthirty.system.auth.util.CaptchaHelper;
import top.kthirty.system.menu.entity.Menu;

import java.util.List;

@RestController
@Tag(name = "认证接口")
@AllArgsConstructor
@RequestMapping("auth")
public class AuthController extends BaseController {
    private final AuthService authService;

    @PostMapping("token")
    @IgnoreSecure
    @Operation(summary = "获取认证token",description = "获取认证token")
    public TokenInfo token(@RequestBody @Valid AuthParam authParam){
        return authService.token(authParam);
    }

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
    public List<Menu> menus(){
        return authService.menus();
    }

}
