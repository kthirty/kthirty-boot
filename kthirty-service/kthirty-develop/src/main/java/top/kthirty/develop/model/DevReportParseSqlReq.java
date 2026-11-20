package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 解析报表 SQL 请求
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@Schema(description = "解析报表SQL请求")
public class DevReportParseSqlReq {

    @NotBlank
    @Schema(description = "报表SQL")
    private String reportSql;

    @Schema(description = "数据源Key，空则使用主数据源")
    private String dsKey;
}
