package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *  实体类。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "")
@Table(value = "sys_dict_item")
public class DictItem extends LogicEntity {

    /**
     * 字典代码
     */
    @Schema(description = "字典代码")
    private String code;

    /**
     * 字典ID
     */
    @Schema(description = "字典ID")
    private String dictId;

    /**
     * 字典值
     */
    @Schema(description = "字典值")
    private String value;

    /**
     * 字典标签
     */
    @Schema(description = "字典标签")
    private String label;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 父ID
     */
    @Schema(description = "父ID")
    private String parentId;

}