package top.kthirty.flowable.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import top.kthirty.core.tool.Func;
import top.kthirty.flowable.util.FlowConstants;

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
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class FlowTask extends TaskEntityImpl {
    private String processInstanceName;
    public FlowTask(Task task, ProcessInstance processInstance){
        super();
        BeanUtil.copyProperties(task,this, FlowConstants.COPY_OPTIONS);
        this.processInstanceName = processInstance.getName();
    }

}
