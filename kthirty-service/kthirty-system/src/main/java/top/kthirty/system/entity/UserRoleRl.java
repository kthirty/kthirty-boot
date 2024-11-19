package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import top.kthirty.core.db.base.entity.IdEntity;

/**
 * 用户角色关联表 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户角色关联表")
@Table(value = "sys_user_role_rl")
public class UserRoleRl extends IdEntity {

    /**
     * 角色代码
     */
    @Schema(description = "角色ID")
    @NotBlank(message = "角色ID不可为空")
    private String roleId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不可为空")
    private String userId;

}
