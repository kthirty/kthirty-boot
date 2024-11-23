package top.kthirty.flowable.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.flowable.common.engine.api.delegate.event.*;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import top.kthirty.core.tool.Func;
import top.kthirty.flowable.util.FlowableHooks;


/**
 * <p>
 * 流程监听器配置
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Configuration
@RequiredArgsConstructor
public class FlowableGlobalListenerConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final SpringProcessEngineConfiguration processEngineConfiguration;
    private final RepositoryService repositoryService;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        FlowableEventDispatcher dispatcher = processEngineConfiguration.getEventDispatcher();
        dispatcher.addEventListener(new FlowableEventListener() {
            @Override
            public void onEvent(FlowableEvent event) {
                if(!(event instanceof FlowableEngineEvent flowableEngineEvent)){
                    return;
                }
                if(Func.isBlank(flowableEngineEvent.getProcessDefinitionId())){
                    return;
                }
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
                        .forEach(hook -> hook.handle(eventType, flowableEngineEvent));
            }
            @Override
            public boolean isFailOnException() {return true;}
            @Override
            public boolean isFireOnTransactionLifecycleEvent() {return true;}
            @Override
            public String getOnTransaction() {return "COMMITTED";}
        });
    }
}
