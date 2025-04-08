package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;

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
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String code;

    /**
     * 角色名
     */
    @Schema(description = "角色名")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String description;

    /**
     * 1正常0停用
     */
    @Schema(description = "状态")
    @ColumnDefine(ColumnDefine.Type.INTEGER)
    private String status;

    @Schema(description = "排序")
    @ColumnDefine(ColumnDefine.Type.INTEGER)
    private Integer sort;
}
