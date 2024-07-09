package top.kthirty.system.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddUserRoleLinkReq implements Serializable {
    @Schema(description = "用户ID")
    @NotEmpty(message = "用户ID不可为空")
    private List<String> userIds;

    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不可为空")
    private String roleId;
}
