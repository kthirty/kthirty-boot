package top.kthirty.system.role.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class RoleConfigMenuVO {
    @Schema(description = "菜单ID集合")
    private List<String> menus;
    @Schema(description = "角色ID")
    @NotBlank
    private String roleId;
}
