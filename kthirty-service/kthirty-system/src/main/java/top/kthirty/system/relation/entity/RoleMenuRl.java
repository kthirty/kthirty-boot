package top.kthirty.system.relation.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色菜单关联表 实体类。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色菜单关联表")
@Table(value = "sys_role_menu_rl")
public class RoleMenuRl extends IdEntity<String> {

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
