package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 报表 Schema
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@Builder
@Schema(description = "报表Schema")
public class DevReportSchemaVO {

    @Schema(description = "报表ID")
    private String reportId;

    @Schema(description = "报表编码")
    private String code;

    @Schema(description = "报表名称")
    private String name;

    @Schema(description = "是否分页")
    private String isPage;

    @Schema(description = "数据源Key")
    private String dsKey;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "字段列表")
    private List<DevReportColumnSchemaVO> columns;
}
