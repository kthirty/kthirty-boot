package top.kthirty.extra.oos.entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.kthirty.core.db.auto.ColumnDefine;
import top.kthirty.core.db.base.entity.LogicEntity;

/**
 * 对象存储文件元数据
 *
 * @author KThirty
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "对象存储文件")
@Table("oos_file")
public class OosFile extends LogicEntity {

    @Schema(description = "原始文件名")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String originalFileName;

    @Schema(description = "存储对象键")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String objectKey;

    @Schema(description = "文件扩展名")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String fileExt;

    @Schema(description = "文件大小（字节）")
    @ColumnDefine(ColumnDefine.Type.LONG)
    private Long fileSize;

    @Schema(description = "MIME 类型")
    @ColumnDefine(ColumnDefine.Type.STRING)
    private String contentType;

    @Schema(description = "存储类型")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String storageType;

    @Schema(description = "存储桶/集合")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String bucket;

    @Schema(description = "文件 MD5")
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    private String md5;
}
