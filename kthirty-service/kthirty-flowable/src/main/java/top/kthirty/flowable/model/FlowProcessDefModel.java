package top.kthirty.flowable.model;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.bpmn.data.IOSpecification;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description 流程定义信息
 * @author KThirty
 * @since 2024/11/22 16:22
 */
@Data
@NoArgsConstructor
public class FlowProcessDefModel {
    private String xml;
    private String thumbnail;
    private String name;
    private String description;
    private String key;
    private int version;
    private String category;
    private String deploymentId;
    private String resourceName;
    private String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    private boolean isGraphicalNotationDefined;
    private boolean hasStartFormKey;
    private int suspensionState = SuspensionState.ACTIVE.getStateCode();
    private String id;

    public FlowProcessDefModel(ProcessDefinition processDefinition){
        this.id = processDefinition.getId();
        this.name = processDefinition.getName();
        this.description = processDefinition.getDescription();
        this.key = processDefinition.getKey();
        this.version = processDefinition.getVersion();
        this.category = processDefinition.getCategory();
        this.deploymentId = processDefinition.getDeploymentId();
        this.resourceName = processDefinition.getResourceName();
        this.tenantId = processDefinition.getTenantId();
        this.hasStartFormKey = processDefinition.hasStartFormKey();
        if(processDefinition instanceof ProcessDefinitionEntityImpl entity){
            this.isGraphicalNotationDefined = entity.isGraphicalNotationDefined();
            this.suspensionState = ObjectUtil.defaultIfNull(entity.getSuspensionState(),SuspensionState.ACTIVE.getStateCode());
        }
    }
}
