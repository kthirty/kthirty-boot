package top.kthirty.flowable.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.history.HistoricProcessInstance;
import top.kthirty.core.db.support.Query;
/**
 * @description 流程定义查询
 * @author KThirty
 * @since 2024/11/22 16:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "流程实例查询参数")
public class FlowProcessInstQuery extends Query<HistoricProcessInstance> {
    @Schema(description = "流程定义名称")
    private String defName;
    @Schema(description = "流程定义KEY")
    private String defKey;
    @Schema(description = "流程定义分类")
    private String defCategory;
    @Schema(description = "流程实例名称")
    private String instName;
    @Schema(description = "是否已完结")
    private Boolean finished;
    @Schema(description = "是否已删除")
    private Boolean deleted;

}
