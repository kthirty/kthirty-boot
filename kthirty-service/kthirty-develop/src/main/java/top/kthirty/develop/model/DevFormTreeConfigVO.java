package top.kthirty.develop.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 树形列表配置
 */
@Data
@Builder
@Schema(description = "树形列表配置")
public class DevFormTreeConfigVO {

    @Schema(description = "父级字段")
    private String parentField;

    @Schema(description = "显示字段")
    private String labelField;

    @Schema(description = "排序字段")
    private String sortField;

    @Schema(description = "根节点父级值")
    private String rootValue;
}
