package top.kthirty.system.dict.entity;

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
@Table(value = "sys_dict")
public class Dict extends LogicEntity {

    /**
     * 字典代码
     */
    @Schema(description = "字典代码")
    private String code;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String name;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

}
