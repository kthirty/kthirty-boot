package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.autotable.annotation.Ignore;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

import java.io.Serial;
import java.util.List;

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
    @Dict(code = "dev_table_type")
    private String tableType;

    /**
     * 列表类型
     * @see top.kthirty.develop.enums.ListType
     */
    @Schema(description = "列表类型")
    @Dict(code = "dev_list_type")
    private String listType;

    /**
     * 是否已同步到数据库
     */
    @Schema(description = "是否已同步到数据库")
    @Dict(code = "whether")
    private String isDbSync;


    @Ignore
    @Schema(description = "表单字段")
    @Singular
    private List<DevFormItem> items;

    @Ignore
    @Schema(description = "表单索引")
    @Singular
    private List<DevFormIndex> indexes;

}
