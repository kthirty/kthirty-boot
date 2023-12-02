package top.kthirty.system.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.secure.annotation.IgnoreSecure;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.auth.model.AuthParam;
import top.kthirty.system.auth.service.AuthService;
import top.kthirty.system.auth.util.CaptchaHelper;

@RestController
@Tag(name = "认证接口")
@AllArgsConstructor
@RequestMapping("auth")
@IgnoreSecure
public class AuthController extends BaseController {
    private final AuthService authService;
    private final RedisUtil redisUtil;

    @PostMapping("token")
    @Operation(summary = "获取认证token",description = "获取认证token")
    public TokenInfo token(@RequestBody @Valid AuthParam authParam){
        return authService.token(authParam);
    }

    @GetMapping("code")
    public String code(){
        return CaptchaHelper.generateCode();
    }


}
