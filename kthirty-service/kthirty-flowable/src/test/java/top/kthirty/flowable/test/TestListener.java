package top.kthirty.flowable.test;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.flowable.model.TaskCompleteReq;
import top.kthirty.flowable.util.FlowableHooks;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TestListener implements FlowableHooks.NativeEventHook
        , FlowableHooks.ProcessCompleteBeforeHook
        , FlowableHooks.ProcessStartAfterHook
        , FlowableHooks.ProcessStartBeforeHook
        , FlowableHooks.ProcessInstanceNameGenerator
        , FlowableHooks.TaskCompleteAfterHook
        , FlowableHooks.TaskCompleteBeforeHook
        , FlowableHooks.TaskCreateBeforeHook
    , FlowableHooks.ProcessDeleteAfterHook
    , FlowableHooks.ProcessDeleteBeforeHook
{
    private final RepositoryService repositoryService;
    @Override
    public List<String> listenProcessDefinitionKey() {
        return List.of("test");
    }

    @Override
    public Map<String, Object> onProcessStartBefore(String processDefinitionKey, String businessKey) {
        log.info("监听到 onProcessStartBefore {} {}",processDefinitionKey,businessKey);
        return null;
    }

    @Override
    public String generateProcessInstanceName(String processDefinitionKey,String processDefinitionName, String businessKey) {
        log.info("监听到 generateProcessInstanceName {} {}",processDefinitionKey,businessKey);
        return processDefinitionName + ":" + businessKey;
    }

    @Override
    public void onProcessStartAfter(String processDefinitionKey, String businessKey, ProcessInstance processInstance) {
        log.info("监听到 onProcessStartAfter {} {} {}",processDefinitionKey,businessKey, processInstance.getProcessInstanceId());
    }

    @Override
    public Map<String, Object> onTaskCompleteBefore(ProcessInstance processInstance, Task task, TaskCompleteReq req) {
        log.info("监听到 onTaskCompleteBefore {} {} {}",processInstance.getProcessInstanceId()
                ,task.getTaskDefinitionId(), JSONUtil.toJsonStr(req));
        return null;
    }

    @Override
    public void onTaskCompleteAfter(ProcessInstance processInstance, Task task, TaskCompleteReq req, Map<String, Object> variables) {
        log.info("监听到 onTaskCompleteBefore processInstance{} task{} req{} variables{}"
                ,processInstance.getProcessInstanceId()
                ,task.getTaskDefinitionId() + "_" + task.getId()
                , JSONUtil.toJsonStr(req)
                ,JSONUtil.toJsonStr(variables));
    }

    @Override
    public void onTaskCreateBefore(ProcessInstance processInstance, FlowElement flowElement, Map<String, Object> variables) {
        log.info("监听到 onTaskCreateBefore processInstance{} flowElement{} variables{}"
                ,processInstance.getProcessInstanceId()
                , flowElement.getId() + flowElement.getName()
                ,JSONUtil.toJsonStr(variables));
    }

    @Override
    public void onProcessCompleteBefore(ProcessInstance processInstance) {
        log.info("监听到 onProcessCompleteBefore processInstance {}",processInstance.getProcessInstanceId());
    }

    @Override
    public void onNativeEvent(FlowableEngineEventType eventType, FlowableEngineEvent flowableEngineEvent) {
        log.info("监听到 onNativeEvent eventType {} flowableEngineEvent{}",eventType,flowableEngineEvent.getClass().getName());
    }

    @Override
    public void onProcessDeleteBefore(ProcessInstance processInstance) {
        log.info("监听到 onProcessDeleteBefore  processInstance{}", processInstance.toString());
    }

    @Override
    public void onProcessDeleteAfter(ProcessInstance processInstance) {
        log.info("监听到 onProcessDeleteAfter  processInstance{}",processInstance.getProcessInstanceId());
    }
}
