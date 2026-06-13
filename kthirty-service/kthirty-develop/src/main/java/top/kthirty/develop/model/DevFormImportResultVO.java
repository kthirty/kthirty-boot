package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 导入结果
 */
@Data
@Builder
@Schema(description = "导入结果")
public class DevFormImportResultVO {

    @Schema(description = "总行数")
    private int total;

    @Schema(description = "成功数")
    private int success;

    @Schema(description = "失败数")
    private int fail;

    @Schema(description = "错误明细")
    private List<ImportError> errors;

    @Data
    @Builder
    public static class ImportError {
        private int row;
        private String column;
        private String message;
    }
}
