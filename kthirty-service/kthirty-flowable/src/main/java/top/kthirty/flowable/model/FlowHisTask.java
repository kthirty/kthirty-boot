package top.kthirty.flowable.model;

import lombok.Data;
import lombok.experimental.Delegate;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.List;
/**
 * @description 流程历史
 * @author KThirty
 * @since 2025/7/7 16:38
 */
@Data
public class FlowHisTask {
    @Delegate
    private HistoricTaskInstance instance;
    private List<Comment> comments;
}
