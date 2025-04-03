package top.kthirty.flowable.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.core.db.support.Query;
/**
 * @description 流程定义查询
 * @author KThirty
 * @since 2024/11/22 15:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowProcessDefQuery extends Query<FlowProcessDefModel> {
    @Schema(description = "流程定义KEY")
    private String key;
    @Schema(description = "流程定义名称")
    private String name;
    @Schema(description = "流程分类")
    private String category;
    @Schema(description = "流程定义是否激活")
    private Boolean active;
}
