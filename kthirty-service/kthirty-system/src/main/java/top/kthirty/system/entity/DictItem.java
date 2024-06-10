package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

import java.util.List;

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
@Schema(description = "字典详情")
@Table(value = "sys_dict_item")
public class DictItem extends LogicEntity {

    /**
     * 字典代码
     */
    @Schema(description = "字典代码")
    private String code;

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
    private Integer weight;

    /**
     * 父ID
     */
    @Schema(description = "父ID")
    private String parentId;


    @Schema(description = "是否有效")
    @Dict(code = "enable_status")
    private String status;


    @Column(ignore = true)
    private List<DictItem> children;

}
