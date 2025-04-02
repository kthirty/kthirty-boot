package top.kthirty.system.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
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
    @ColumnDefine(type = "varchar", length = 32, notNull = true)
    private String code;

    /**
     * 字典值
     */
    @Schema(description = "字典值")
    @ColumnDefine(type = "varchar", length = 100)
    private String value;

    /**
     * 字典标签
     */
    @Schema(description = "字典标签")
    @ColumnDefine(type = "varchar", length = 100)
    private String label;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(type = "varchar", length = 200)
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @ColumnDefine(type = "int")
    private Integer weight;

    /**
     * 父ID
     */
    @Schema(description = "父ID")
    @ColumnDefine(type = "varchar", length = 32, defaultValue = "0")
    private String parentId;


    @Schema(description = "是否有效")
    @Dict(code = "enable_status")
    @ColumnDefine(type = "varchar", length = 10)
    private String status;


    @Column(ignore = true)
    private List<DictItem> children;

}
