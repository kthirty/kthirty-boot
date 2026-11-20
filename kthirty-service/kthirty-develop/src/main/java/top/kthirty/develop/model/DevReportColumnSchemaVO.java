package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 报表字段 Schema
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@Builder
@Schema(description = "报表字段Schema")
public class DevReportColumnSchemaVO {

    @Schema(description = "字段名")
    private String columnName;

    @Schema(description = "前端字段名(camelCase)")
    private String fieldName;

    @Schema(description = "显示标签")
    private String label;

    @Schema(description = "字段类型")
    private String fieldType;

    @Schema(description = "列表显示")
    private boolean showInList;

    @Schema(description = "允许排序")
    private boolean allowSort;

    @Schema(description = "是否搜索")
    private boolean showInQuery;

    @Schema(description = "搜索组件")
    private String queryComponent;

    @Schema(description = "列表显示组件")
    private String listComponent;

    @Schema(description = "字典Code")
    private String dictCode;

    @Schema(description = "排序权重")
    private Double weight;
}
