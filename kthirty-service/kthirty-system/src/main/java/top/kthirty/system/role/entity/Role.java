package top.kthirty.system.role.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色")
@Table(value = "sys_role")
public class Role extends LogicEntity {

    /**
     * 代码(上级关系)
     */
    @Schema(description = "代码(上级关系)")
    private String code;

    /**
     * 角色代码
     */
    @Schema(description = "角色代码")
    private String enName;

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;

    /**
     * 上级角色ID
     */
    @Schema(description = "上级角色ID")
    private String parentId;

}
