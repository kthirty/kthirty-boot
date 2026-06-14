package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码生成选项
 */
@Data
@Schema(description = "代码生成选项")
public class DevFormCodegenOption {

    @Schema(description = "后端包名")
    private String backendPackage = "top.kthirty.develop";

    @Schema(description = "前端模块目录(views下)")
    private String frontendModule = "develop";
}
