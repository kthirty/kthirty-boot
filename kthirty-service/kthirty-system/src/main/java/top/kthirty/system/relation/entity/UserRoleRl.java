package top.kthirty.system.relation.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;

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
    @Schema(description = "角色代码")
    private String roleCode;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

}
