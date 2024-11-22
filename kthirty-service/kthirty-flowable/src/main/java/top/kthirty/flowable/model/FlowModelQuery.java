package top.kthirty.flowable.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.repository.Model;
import top.kthirty.core.db.support.Query;
/**
 * @description 模型文件查询
 * @author KThirty
 * @since 2024/11/21 15:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowModelQuery extends Query<Model> {
    @Schema(description = "模型KEY")
    private String key;
    @Schema(description = "模型名称")
    private String name;
    @Schema(description = "模型分类")
    private String category;
    @Schema(description = "是否已部署")
    private Boolean deployed;
    @Schema(description = "租户ID")
    private String tenantId;
}
