package top.kthirty.system.relation.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.IdEntity;

/**
 * 角色菜单关联表 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色菜单关联表")
@Table(value = "sys_role_menu_rl")
public class RoleMenuRl extends IdEntity {

    /**
     * 角色代码
     */
    @Schema(description = "角色代码")
    private String roleCode;

    /**
     * 菜单ID
     */
    @Schema(description = "菜单ID")
    private String menuId;

}
