package top.kthirty.flowable.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.flowable.model.FlowModel;
import top.kthirty.flowable.model.TaskCompleteReq;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author KThirty
 * @description 流程相关工具
 * @since 2024/11/26 9:10
 */
@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class FlowableHelper {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    /**
     * 流程启动
     *
     * @param processDefinitionKey 流程定义KEY
     * @param businessKey          业务流程KEY
     * @author Kthirty
     * @since 2024/11/23
     */
    public ProcessInstance start(String processDefinitionKey, String businessKey) {
        Assert.notBlank(processDefinitionKey, "流程定义KEY不可为空");
        Assert.notBlank(businessKey, "业务流程KEY不可为空");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).latestVersion().singleResult();
        Assert.notNull(processDefinition, "流程定义不存在");
        Assert.isTrue(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).count() == 0, "当前业务流程KEY已存在正在运行的流程，无法重复发起");
        Kv variables = Kv.init();
        // 执行前置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartBeforeHook.class, processDefinitionKey)
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.onProcessStartBefore(processDefinitionKey, businessKey), Map.of())));
        // 启动流程
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionKey(processDefinitionKey)
                .businessKey(businessKey)
                .variables(variables)
                .tenantId(SecureUtil.getTenantId())
                .owner(SecureUtil.getUsername())
                .name(FlowableUtil.getProcessName(processDefinition, businessKey, variables))
                .start();
        // 执行后置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartAfterHook.class, processDefinitionKey)
                .forEach(it -> it.onProcessStartAfter(processDefinitionKey, businessKey, processInstance));
        log.info("流程启动成功，流程定义KEY：{}，业务流程KEY：{}，流程实例ID：{}", processDefinitionKey, businessKey, processInstance.getProcessInstanceId());
        return processInstance;
    }

    /**
     * 任务办理
     *
     * @param req 办理参数
     * @author Kthirty
     * @since 2024/11/23
     */
    public void complete(TaskCompleteReq req) {
        Task task = taskService.createTaskQuery().taskId(req.getTaskId()).singleResult();
        Assert.notNull(task, "任务不存在");
        Assert.isFalse(task.isSuspended(), "任务已挂起，无法办理");
        // 验证任务是否已签收
        if (!SecureUtil.isSuperAdmin() && StrUtil.isNotBlank(task.getClaimedBy())) {
            Assert.isTrue(task.getClaimedBy().equals(SecureUtil.getUsername()), "任务已被他人领取，无法办理");
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        Assert.notNull(processInstance, "流程实例不存在");
        Kv variables = Kv.init().set(FlowConstants.TASK_COMPLETE_VAR_NAME, req.getResult());
        // 执行前置钩子
        FlowableHooks.getHooks(FlowableHooks.TaskCompleteBeforeHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.onTaskCompleteBefore(processInstance, task, req), Map.of())));
        // 执行下一个节点的创建前钩子
        FlowElement nextNode = FlowableUtil.getNextNode(task.getId(), variables);
        Assert.notNull(nextNode, "流程异常，下一个节点为空");
        FlowableHooks.getHooks(FlowableHooks.TaskCreateBeforeHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> hook.onTaskCreateBefore(processInstance, nextNode, variables));
        // 流程完成前钩子
        Func.doIf(nextNode instanceof EndEvent, () -> FlowableHooks.getHooks(FlowableHooks.ProcessCompleteBeforeHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> hook.onProcessCompleteBefore(processInstance)));
        // 添加批注
        if (StrUtil.isNotBlank(req.getComment())) {
            taskService.addComment(req.getTaskId(), processInstance.getProcessInstanceId(), req.getComment());
        }
        // 任务处理
        taskService.setVariable(req.getTaskId(),FlowConstants.TASK_COMPLETE_VAR_NAME, req.getResult());
        taskService.complete(req.getTaskId(), SecureUtil.getUsername(), variables);
        // 执行后置钩子
        FlowableHooks.getHooks(FlowableHooks.TaskCompleteAfterHook.class, processInstance.getProcessDefinitionKey())
                .forEach(hook -> hook.onTaskCompleteAfter(processInstance, task, req, variables));
    }

    /**
     * 保存模型
     * @param flowModel 流程模型
     * @return FlowModel
     */
    public FlowModel saveModel(FlowModel flowModel) {
        Assert.notBlank(flowModel.getXml(),"流程模型XML不能为空");
        BpmnModel bpmnModel = FlowableUtil.getBpmnModelByXml(flowModel.getXml());
        Process mainProcess = bpmnModel.getMainProcess();
        if(StrUtil.isBlank(flowModel.getKey())){
            flowModel.setKey(mainProcess.getId());
        }
        if(StrUtil.isBlank(flowModel.getName())){
            flowModel.setName(mainProcess.getName());
        }

        Model model = repositoryService.createModelQuery().modelKey(flowModel.getKey()).count() != 0
                ? repositoryService.createModelQuery().modelKey(flowModel.getKey()).singleResult()
                : repositoryService.newModel();
        model.setCategory(flowModel.getCategory());
        model.setKey(flowModel.getKey());
        model.setName(flowModel.getName());
        model.setMetaInfo(flowModel.getMetaInfo());
        model.setTenantId(flowModel.getTenantId());
        repositoryService.saveModel(model);
        flowModel.setId(model.getId());
        if(StrUtil.isNotBlank(flowModel.getXml())){
            repositoryService.addModelEditorSource(model.getId(),flowModel.getXml().getBytes(StandardCharsets.UTF_8));
        }
        return flowModel;
    }
}
