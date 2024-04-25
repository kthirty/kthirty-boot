package top.kthirty.examples.web.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.secure.annotation.IgnoreSecure;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.core.secure.token.TokenUtil;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.examples.web.user.entity.SystemUser;
import top.kthirty.examples.web.user.mapper.SysUserMapper;

import java.util.List;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
public class UserController extends BaseController   {
    private final SysUserMapper sysUserMapper;

    @GetMapping("list")
    public List<SystemUser> list(){
        return sysUserMapper.selectAll();
    }

    @GetMapping("auth")
    @IgnoreSecure
    public TokenInfo auth(String username){
        return TokenUtil.authorize(new SysUser().setUsername(username));
    }
}
