package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 动态表单功能开关
 */
@Data
@Builder
@Schema(description = "动态表单功能开关")
public class DevFormFeaturesVO {

    @Schema(description = "允许新增")
    private boolean enableAdd;

    @Schema(description = "允许编辑")
    private boolean enableEdit;

    @Schema(description = "允许删除")
    private boolean enableDelete;

    @Schema(description = "允许导出")
    private boolean enableExport;

    @Schema(description = "允许导入")
    private boolean enableImport;

    @Schema(description = "允许批量删除")
    private boolean enableBatchDelete;
}
