package top.kthirty.flowable.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.tool.Func;
import top.kthirty.flowable.util.FlowableHooks;

/**
 * @description 全局流程监听
 * @author KThirty
 * @since 2024/11/26 13:23
 */
@Transactional(rollbackFor = Exception.class)
public class FlowableGlobalListener implements FlowableEventListener {
    @Override
    public void onEvent(FlowableEvent event) {
        if(!(event instanceof FlowableEngineEvent flowableEngineEvent)){
            return;
        }
        if(Func.isBlank(flowableEngineEvent.getProcessDefinitionId())){
            return;
        }
        String processDefinitionKey = StrUtil.subBefore(flowableEngineEvent.getProcessDefinitionId(), ":", false);
        FlowableEngineEventType eventType = FlowableEngineEventType.valueOf(event.getType().name());
        FlowableHooks.getHooks(FlowableHooks.NativeEventHook.class, processDefinitionKey).forEach(hook -> {
            if(CollUtil.isEmpty(hook.listenEventTypes()) || hook.listenEventTypes().contains(eventType)){
                hook.onNativeEvent(eventType, flowableEngineEvent);
            }
        });
    }
    @Override
    public boolean isFailOnException() {return true;}
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {return false;}
    @Override
    public String getOnTransaction() {return null;}
}
