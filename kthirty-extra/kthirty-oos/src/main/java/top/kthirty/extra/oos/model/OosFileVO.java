package top.kthirty.extra.oos.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传结果
 *
 * @author KThirty
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件上传结果")
public class OosFileVO {

    @Schema(description = "文件 ID")
    private String id;

    @Schema(description = "原始文件名（前端展示用）")
    private String fileName;

    @Schema(description = "MIME 类型")
    private String contentType;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;
}
