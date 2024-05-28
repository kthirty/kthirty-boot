package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户角色关联表")
@Table(value = "sys_user_role_rl")
public class UserRoleRl extends IdEntity {

    /**
     * 角色代码
     */
    @Schema(description = "角色ID")
    private String roleId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

}
