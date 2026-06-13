package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 动态表单完整 Schema
 */
@Data
@Builder
@Schema(description = "动态表单Schema")
public class DevFormSchemaVO {

    @Schema(description = "表单ID")
    private String formId;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "表类型")
    private String tableType;

    @Schema(description = "列表类型")
    private String listType;

    @Schema(description = "是否已同步数据库")
    private String isDbSync;

    @Schema(description = "主键字段(数据库列名)")
    private String primaryKey;

    @Schema(description = "主键前端字段名")
    private String primaryKeyField;

    @Schema(description = "逻辑删除字段")
    private String logicDeleteField;

    @Schema(description = "审计字段")
    private List<String> auditFields;

    @Schema(description = "树形配置")
    private DevFormTreeConfigVO treeConfig;

    @Schema(description = "字段列表")
    private List<DevFormColumnSchemaVO> columns;

    @Schema(description = "功能开关")
    private DevFormFeaturesVO features;
}
