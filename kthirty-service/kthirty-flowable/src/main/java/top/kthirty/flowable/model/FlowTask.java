package top.kthirty.flowable.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

import java.util.Date;

/**
 * <p>
 * 任务实体
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class FlowTask  {
    private String processInstanceName;
    private String processDefinitionName;
    private String owner;
    private String assignee;
    private String name;
    private String description;
    private int priority;
    private String state;
    private Date createTime;
    private Date claimTime;
    private String claimedBy;
    private Date suspendedTime;
    private String suspendedBy;
    private Date dueDate;
    private int suspensionState;
    private String category;
    private String executionId;
    private String processInstanceId;
    private String processDefinitionId;
    private String taskDefinitionId;
    private String taskDefinitionKey;
    private String formKey;
    private String tenantId;

    public FlowTask(Task task, ProcessInstance processInstance){
        this.processInstanceName = processInstance.getName();
        this.processDefinitionName = processInstance.getProcessDefinitionName();
        this.owner = task.getOwner();
        this.assignee = task.getAssignee();
        this.name = task.getName();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.state = task.getState();
        this.createTime = task.getCreateTime();
        this.claimTime = task.getClaimTime();
        this.claimedBy = task.getClaimedBy();
        this.suspendedTime = task.getSuspendedTime();
        this.suspendedBy = task.getSuspendedBy();
        this.dueDate = task.getDueDate();
        this.category = task.getCategory();
        this.executionId = task.getExecutionId();
        this.processInstanceId = task.getProcessInstanceId();
        this.processDefinitionId = task.getProcessDefinitionId();
        this.taskDefinitionId = task.getTaskDefinitionId();
        this.taskDefinitionKey = task.getTaskDefinitionKey();
        this.formKey = task.getFormKey();
        this.tenantId = task.getTenantId();
        if(task instanceof TaskEntity taskEntity){
            this.suspensionState = taskEntity.getSuspensionState();
        }
    }

}
