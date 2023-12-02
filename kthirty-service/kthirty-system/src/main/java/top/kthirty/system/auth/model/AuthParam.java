package top.kthirty.system.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthParam {
    @Schema(title = "用户名")
    @NotBlank(message = "用户名不可为空")
    private String username;
    @Schema(title = "密码")
    @NotBlank(message = "密码不可为空")
    private String password;
    @Schema(title = "验证码")
    private String code;
}
