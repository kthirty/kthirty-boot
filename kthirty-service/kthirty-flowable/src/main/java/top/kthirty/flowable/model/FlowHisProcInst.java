package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;

/**
 * @description 流程实例信息
 * @author KThirty
 * @since 2025/7/4 13:52
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class FlowHisProcInst extends HistoricProcessInstanceEntityImpl {
    private Boolean suspended;

    public FlowHisProcInst(HistoricProcessInstance processInstance){
        this.id = processInstance.getId();
        this.processInstanceId = processInstance.getId();
        this.businessKey = processInstance.getBusinessKey();
        this.businessStatus = processInstance.getBusinessStatus();
        this.name = processInstance.getName();
        this.processDefinitionId = processInstance.getProcessDefinitionId();
        this.processDefinitionKey = processInstance.getProcessDefinitionKey();
        this.processDefinitionName = processInstance.getProcessDefinitionName();
        this.processDefinitionVersion = processInstance.getProcessDefinitionVersion();
        this.processDefinitionCategory = processInstance.getProcessDefinitionCategory();
        this.deploymentId = processInstance.getDeploymentId();
        this.startActivityId = processInstance.getStartActivityId();
        this.startTime = processInstance.getStartTime();
        this.endTime = processInstance.getEndTime();
        this.deleteReason = processInstance.getDeleteReason();
        this.durationInMillis = processInstance.getDurationInMillis();
        this.description = processInstance.getDescription();
        this.startUserId = processInstance.getStartUserId();
        this.superProcessInstanceId = processInstance.getSuperProcessInstanceId();
        this.callbackId = processInstance.getCallbackId();
        this.callbackType = processInstance.getCallbackType();
        this.referenceId = processInstance.getReferenceId();
        this.referenceType = processInstance.getReferenceType();
        this.propagatedStageInstanceId = processInstance.getPropagatedStageInstanceId();

        if(processInstance instanceof HistoricProcessInstanceEntityImpl){
            this.isDeleted = ((HistoricProcessInstanceEntityImpl) processInstance).isDeleted();
        }

        // Inherit tenant id (if applicable)
        if (processInstance.getTenantId() != null) {
            this.tenantId = processInstance.getTenantId();
        }
    }
}
