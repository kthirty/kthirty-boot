package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.core.db.support.Query;

import java.util.Date;

/**
 * 流程todo 查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowTodoQuery extends Query<FlowTask> {
    private String taskName;
    private String taskDefKey;
    private String processDefKey;
    private String processDefName;
    private String processInstName;
    private String processInstId;
    private Boolean active;
    private String businessKey;
    private Date[] taskCreateTime;

    public Date getTaskCreateTimeStart() {
        return taskCreateTime != null ? taskCreateTime[0] : null;
    }
    public Date getTaskCreateTimeEnd() {
        return taskCreateTime != null ? taskCreateTime[1] : null;
    }

}
