package top.kthirty.system.entity;

import com.mybatisflex.annotation.Table;
import com.tangzc.mybatisflex.autotable.annotation.UniIndex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;

/**
 * 字典 实体类。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "字典")
@Table(value = "sys_dict")
public class Dict extends LogicEntity {

    /**
     * 字典代码
     */
    @Schema(description = "字典代码")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    @UniIndex(name = "sys_dict_uni_code")
    private String code;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String description;

}
