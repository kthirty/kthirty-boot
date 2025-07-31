package top.kthirty.develop.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;
/**
 * @description 表单开发索引
 * @author KThirty
 * @since 2025/6/27 10:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "form开发")
@Table("dev_form_index")
@Accessors(chain = true)
public class DevFormIndex extends LogicEntity {

    /**
     * @see top.kthirty.develop.enums.IndexType
     */
    @Schema(description = "索引类型")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String indexType;

    @Schema(description = "索引字段")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String columnNames;

    @Schema(description = "索引名称")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String indexName;

    @Schema(description = "dev_form表ID")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String formId;

    /**
     * 是否允许排序
     */
    @Schema(description = "是否已同步到数据库")
    private Boolean isDbSync;
}
