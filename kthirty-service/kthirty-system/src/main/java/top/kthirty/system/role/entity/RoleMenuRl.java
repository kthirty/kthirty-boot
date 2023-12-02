package top.kthirty.system.role.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kthirty.core.db.base.entity.IdEntity;

/**
 * 角色菜单关联表 实体类。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色菜单关联表")
@Table(value = "sys_role_menu_rl")
public class RoleMenuRl extends IdEntity<String> {

    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "菜单ID")
    private String menuId;

}
