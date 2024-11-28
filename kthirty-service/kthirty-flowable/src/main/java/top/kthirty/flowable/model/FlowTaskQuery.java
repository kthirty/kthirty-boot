package top.kthirty.flowable.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.TaskQuery;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代办流程查询
 * </p>
 *
 * @author KThirty
 * @since 2024/11/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowTaskQuery extends Query<FlowTask> {
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

    @SuppressWarnings("unchecked")
    public void handleTaskQuery(TaskInfoQuery taskQuery){
        Func.doNotBlank(this.getProcessDefKey(), () -> taskQuery.processDefinitionKey(this.getProcessDefKey()));
        Func.doNotBlank(this.getProcessDefName(), () -> taskQuery.processDefinitionNameLike(this.getProcessDefName()));
        Func.doNotBlank(this.getProcessInstId(), () -> taskQuery.processInstanceIdIn(StrUtil.splitTrim(this.getProcessInstId(), StringPool.COMMA)));
        Func.doNotBlank(this.getTaskName(), () -> taskQuery.taskNameLike(this.getTaskName()));
        Func.doNotBlank(this.getTaskDefKey(), () -> taskQuery.taskDefinitionKeys(StrUtil.splitTrim(this.getTaskDefKey(), StringPool.COMMA)));
        Func.doNotBlank(this.getBusinessKey(), () -> taskQuery.processInstanceBusinessKey(this.getBusinessKey()));
        Func.doNotNull(this.getTaskCreateTimeStart(), () -> taskQuery.taskCreatedAfter(this.getTaskCreateTimeStart()));
        Func.doNotNull(this.getTaskCreateTimeEnd(), () -> taskQuery.taskCreatedBefore(this.getTaskCreateTimeEnd()));
        Func.doNotBlank(this.getProcessInstName(), () -> {
            RuntimeService runtimeService = SpringUtil.getBeanSafe(RuntimeService.class);
            if(runtimeService != null){
                List<String> procInstIds = CollUtil.defaultIfEmpty(runtimeService.createProcessInstanceQuery()
                        .processInstanceNameLike(this.getProcessInstName())
                        .list()
                        .stream()
                        .map(Execution::getProcessInstanceId)
                        .toList(), CollUtil.toList(Constant.NOT_FOUND));
                taskQuery.processInstanceIdIn(procInstIds);
            }
        });
        if(taskQuery instanceof TaskQuery runTaskQuery){
            Func.doIf(this.getActive() != null && this.getActive(), runTaskQuery::active);
            Func.doIf(this.getActive() != null && !this.getActive(), runTaskQuery::suspended);
        }
    }

}
