package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import top.kthirty.core.db.base.entity.LogicEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * form开发 实体类。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "form开发")
@Table("dev_form")
public class DevForm extends LogicEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 表名
     */
    @Schema(description = "表名")
    private String tableName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remarks;

    /**
     * 表类型
     * @see top.kthirty.develop.enums.TableType
     */
    @Schema(description = "表类型 1单表2主表3附表")
    private String tableType;

    /**
     * 列表类型
     * @see top.kthirty.develop.enums.ListType
     */
    @Schema(description = "列表类型")
    private String listType;

    /**
     * 是否已同步到数据库
     */
    @Schema(description = "是否已同步到数据库")
    private Integer isDbSync;

}
