package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 表结构同步结果
 */
@Data
@Builder
@Schema(description = "表结构同步结果")
public class DevFormSyncResultVO {

    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "表名")
    private String tableName;

    @Schema(description = "是否新建表")
    private boolean created;

    @Schema(description = "新增字段")
    private List<String> addedColumns;

    @Schema(description = "新增索引")
    private List<String> addedIndexes;

    @Schema(description = "提示信息")
    private List<String> messages;
}
