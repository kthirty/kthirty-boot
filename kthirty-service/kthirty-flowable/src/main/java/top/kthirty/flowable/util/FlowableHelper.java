package top.kthirty.flowable.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.flowable.model.TaskCompleteReq;

import java.util.Map;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FlowableHelper {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    /**
     * 流程启动
     * @author Kthirty
     * @since 2024/11/23
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey 业务流程KEY
     */
    public void start(String processDefinitionKey, String businessKey) {
        Assert.notBlank(processDefinitionKey, "流程定义KEY不可为空");
        Assert.notBlank(businessKey, "业务流程KEY不可为空");
        Assert.isTrue(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).count() == 0, "当前业务流程KEY已存在正在运行的流程，无法重复发起");
        Kv variables = Kv.init();
        // 执行前置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartBeforeHook.class, processDefinitionKey)
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.handle(processDefinitionKey, businessKey), Map.of())));
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // 执行流程实例名称钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessInstanceNameGenerator.class, processDefinitionKey)
                .forEach(it -> runtimeService.setProcessInstanceName(processInstance.getProcessInstanceId(), it.generate(processDefinitionKey, businessKey)));
        // 执行后置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartAfterHook.class, processDefinitionKey)
                .forEach(it -> it.handle(processDefinitionKey, businessKey, processInstance));
    }
    /**
     * 任务办理
     * @author Kthirty
     * @since 2024/11/23
     * @param req 办理参数
     */
    public void complete(TaskCompleteReq req) {
        Task task = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult();
        Assert.notNull(task, "任务不存在");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Assert.notNull(processInstance, "流程实例不存在");
        Kv variables = Kv.init().set(FlowConstants.TASK_COMPLETE_VAR_NAME, req.getResult());
        // 执行前置钩子
        FlowableHooks.getHooks(FlowableHooks.TaskCompleteBeforeHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.handle(processInstance, task, req), Map.of())));
        // 执行下一个节点的创建前钩子
        FlowElement nextNode = FlowableUtil.getNextNode(task.getId(), variables);
        Assert.notNull(nextNode, "流程异常，下一个节点为空");
        FlowableHooks.getHooks(FlowableHooks.TaskCreateBeforeHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> hook.handle(processInstance, nextNode, variables));
        // 流程完成前钩子
        Func.doIf(nextNode instanceof EndEvent, () -> FlowableHooks.getHooks(FlowableHooks.ProcessCompleteBeforeHook.class,processInstance.getProcessDefinitionKey())
                .forEach(hook -> hook.handle(processInstance)));
        // 任务处理
        taskService.complete(req.getTaskId(), SecureUtil.getUsername(), variables);
        if(StrUtil.isNotBlank(req.getComment())){
            taskService.addComment(req.getTaskId(), processInstance.getProcessInstanceId(), req.getComment());
        }
        // 执行后置钩子
        FlowableHooks.getHooks(FlowableHooks.TaskCompleteAfterHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.handle(processInstance, task, req, variables), Map.of())));
    }


}
