package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 动态表单字段 Schema
 */
@Data
@Builder
@Schema(description = "动态表单字段Schema")
public class DevFormColumnSchemaVO {

    @Schema(description = "数据库字段名")
    private String columnName;

    @Schema(description = "前端字段名(camelCase)")
    private String fieldName;

    @Schema(description = "显示标签")
    private String label;

    @Schema(description = "Java字段类型")
    private String fieldType;

    @Schema(description = "字段属性")
    private String fieldAttribute;

    @Schema(description = "列表显示")
    private boolean showInList;

    @Schema(description = "允许排序")
    private boolean allowSort;

    @Schema(description = "查询显示")
    private boolean showInQuery;

    @Schema(description = "查询组件")
    private String queryComponent;

    @Schema(description = "表单显示")
    private boolean showInForm;

    @Schema(description = "表单组件")
    private String formComponent;

    @Schema(description = "是否必填")
    private boolean required;

    @Schema(description = "是否只读")
    private boolean readonly;

    @Schema(description = "正则验证")
    private String regexp;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "字典Code")
    private String dictCode;

    @Schema(description = "是否允许为空")
    private boolean nullable;

    @Schema(description = "字段长度")
    private Integer maxLength;

    @Schema(description = "小数位数")
    private Integer pointLength;

    @Schema(description = "排序权重")
    private Double weight;
}
