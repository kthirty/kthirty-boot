package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 角色 实体类。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色")
@Table(value = "sys_role")
public class Role extends LogicEntity {

    /**
     * 代码
     */
    @Schema(description = "代码")
    private String code;

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 1正常0停用
     */
    @Schema(description = "状态")
    private String status;

    @Schema(description = "排序")
    private Integer sort;
}
