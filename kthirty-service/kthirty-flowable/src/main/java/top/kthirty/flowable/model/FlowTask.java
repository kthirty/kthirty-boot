package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import top.kthirty.core.tool.Func;

/**
 * <p>
 * 任务实体
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class FlowTask extends TaskEntityImpl {
    private String processInstanceName;
    public FlowTask(Task task, ProcessInstance processInstance){
        Func.copy(task,this);
        this.processInstanceName = processInstance.getName();
    }

}
