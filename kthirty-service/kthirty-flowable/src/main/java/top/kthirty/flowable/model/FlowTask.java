package top.kthirty.flowable.model;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import top.kthirty.core.tool.utils.SpringUtil;

import java.util.*;
import java.util.stream.Collectors;

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
    @Schema(title = "任务ID")
    private String taskId;
    @Schema(title = "任务ID")
    private String id;
    @Schema(title = "流程实例名称")
    private String processInstanceName;
    @Schema(title = "流程定义名称")
    private String processDefinitionName;
    @Schema(title = "任务归属")
    private String owner;
    @Schema(title = "任务办理人")
    private String assignee;
    @Schema(title = "任务名称")
    private String name;
    @Schema(title = "任务描述")
    private String description;
    @Schema(title = "任务优先级")
    private int priority;
    @Schema(title = "任务状态")
    private String state;
    @Schema(title = "任务创建时间")
    private Date createTime;
    @Schema(title = "任务结束时间")
    private Date endTime;
    @Schema(title = "任务认领时间")
    private Date claimTime;
    @Schema(title = "任务认领人")
    private String claimedBy;
    @Schema(title = "任务挂起时间")
    private Date suspendedTime;
    @Schema(title = "任务挂起人")
    private String suspendedBy;
    @Schema(title = "任务截止时间")
    private Date dueDate;
    @Schema(title = "暂停状态")
    private int suspensionState;
    @Schema(title = "分类")
    private String category;
    @Schema(title = "执行实例ID")
    private String executionId;
    @Schema(title = "流程实例ID")
    private String processInstanceId;
    @Schema(title = "流程定义ID")
    private String processDefinitionId;
    @Schema(title = "任务定义ID")
    private String taskDefinitionId;
    @Schema(title = "任务定义KEY")
    private String taskDefinitionKey;
    @Schema(title = "表单KEY")
    private String formKey;
    @Schema(title = "租户ID")
    private String tenantId;
    @Schema(title = "流程开始人")
    private String processInstanceStartedBy;
    @Schema(title = "流程开始时间")
    protected Date processInstanceStartTime;

    public FlowTask(TaskInfo task){
        this.taskId = task.getId();
        this.id = task.getId();
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
        if(task instanceof HistoricTaskInstance hisTask){
            this.endTime = hisTask.getEndTime();
        }
    }

    public static List<FlowTask> fullProcessInstanceInfo(List<FlowTask> list){
        if(CollUtil.isEmpty(list)){
            return new ArrayList<>();
        }
        // 查询流程实例信息
        HistoryService historyService = SpringUtil.getBeanSafe(HistoryService.class);
        if(historyService != null){
            Map<String, HistoricProcessInstance> procInstMap = new HashMap<>(list.size());
            historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(list.stream().map(FlowTask::getProcessInstanceId).collect(Collectors.toSet()))
                    .list()
                    .forEach(it -> procInstMap.put(it.getId(), it));
            list.forEach(it -> {
                HistoricProcessInstance processInstance = procInstMap.get(it.getProcessInstanceId());
                it.setProcessInstanceName(processInstance.getName());
                it.setProcessDefinitionName(processInstance.getProcessDefinitionName());
                it.setProcessInstanceStartedBy(processInstance.getStartUserId());
                it.setProcessInstanceStartTime(processInstance.getStartTime());
            });
        }
        return list;
    }

}
