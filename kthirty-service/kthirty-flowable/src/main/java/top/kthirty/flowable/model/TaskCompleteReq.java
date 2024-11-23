package top.kthirty.flowable.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;
/**
 * <p>
 * 任务办理请求
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Data
public class TaskCompleteReq {
    @Schema(description = "任务ID")
    @NotBlank(message = "任务ID不能为空")
    private String taskId;
    @Schema(description = "办理意见")
    private String comment;
    @Schema(description = "办理结果")
    @NotBlank(message = "办理结果不能为空")
    private String result;
    @Schema(description = "额外提交信息（流程不处理，由业务钩子进行处理）")
    private Map<String, Object> extraParams;
}
