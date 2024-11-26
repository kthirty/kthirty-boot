package top.kthirty.flowable.config;

import lombok.AllArgsConstructor;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.flowable.util.FlowableHooks;
/**
 * @description 全局流程监听
 * @author KThirty
 * @since 2024/11/26 13:23
 */
public class FlowableGlobalListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent event) {
        if(!(event instanceof FlowableEngineEvent flowableEngineEvent)){
            return;
        }
        if(Func.isBlank(flowableEngineEvent.getProcessDefinitionId())){
            return;
        }
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(flowableEngineEvent.getProcessDefinitionId()).singleResult();
        if(processDefinition == null){
            return;
        }
        FlowableEngineEventType eventType = FlowableEngineEventType.valueOf(event.getType().name());
        // 执行钩子函数
        FlowableHooks.getHooks(FlowableHooks.NativeEventHook.class, processDefinition.getKey())
                .forEach(hook -> hook.onNativeEvent(eventType, flowableEngineEvent));
    }
    @Override
    public boolean isFailOnException() {return true;}
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {return true;}
    @Override
    public String getOnTransaction() {return "COMMITTED";}
}
