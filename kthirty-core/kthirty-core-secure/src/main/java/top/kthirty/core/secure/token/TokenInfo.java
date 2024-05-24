package top.kthirty.core.secure.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TokenInfo {
    @Schema(title = "令牌")
    private String accessToken;
    @Schema(title = "刷新令牌")
    private String refreshToken;
    @Schema(title = "过期时间(秒)")
    private Integer expiresIn;
    @Schema(title = "许可证")
    private String license = "Powered by KTHIRTY";
    @Schema(title = "用户名")
    private String username;
    @Schema(title = "用户ID")
    private String userId;
}
