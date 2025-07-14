package top.kthirty.flowable.controller;

import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.tool.Func;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.flowable.model.FlowHisProcInst;
import top.kthirty.flowable.model.FlowHisTask;
import top.kthirty.flowable.model.FlowProcessInstQuery;
import top.kthirty.flowable.util.FlowableHooks;
import top.kthirty.flowable.util.FlowableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author KThirty
 * @description 流程实例控制器
 * @since 2024/11/22 16:39
 */
@RestController
@RequestMapping("flw/process/instance")
@RequiredArgsConstructor
@Tag(name = "流程实例")
@Transactional(rollbackFor = Exception.class)
public class ProcessInstanceController extends BaseController {
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final TaskService taskService;

    @GetMapping("page")
    @Operation(summary = "分页查询流程实例")
    public Page<FlowHisProcInst> page(FlowProcessInstQuery req) {
        Set<String> suspendedInstanceIds = runtimeService.createProcessInstanceQuery().suspended().list().stream().map(Execution::getProcessInstanceId).collect(Collectors.toSet());

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

        Func.doNotBlank(req.getId(), () -> query.processInstanceId(req.getId()));
        Func.doNotBlank(req.getProcessDefinitionCategory(), () -> query.processDefinitionCategory(req.getProcessDefinitionCategory()));
        Func.doNotBlank(req.getProcessDefinitionKey(), () -> query.processDefinitionKeyLike(req.getProcessDefinitionKey()));
        Func.doNotBlank(req.getProcessDefinitionName(), () -> query.processDefinitionNameLike(req.getProcessDefinitionName()));
        Func.doNotBlank(req.getName(), () -> query.processInstanceNameLike(req.getName()));
        Func.doNotBlank(req.getStartUserId(), () -> query.startedBy(req.getStartUserId()));
        Func.doIf(req.getFinished() != null && req.getFinished(), query::finished);
        Func.doIf(req.getFinished() != null && !req.getFinished(), query::unfinished);
        Func.doIf(req.getDeleted() != null && req.getDeleted(), query::deleted);
        Func.doIf(req.getDeleted() != null && !req.getDeleted(), query::notDeleted);
        query.orderByProcessInstanceStartTime().desc();
        List<FlowHisProcInst> historicProcessInstances = query.listPage(req.getFirstResult(), req.getPageSize())
                .stream()
                .map(it -> new FlowHisProcInst(it).setSuspended(suspendedInstanceIds.contains(it.getId())))
                .collect(Collectors.toList());
        return req.getPage(query.count(), historicProcessInstances);
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除流程实例")
    @Transactional
    public void delete(String procInstId, String deleteReason) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        Assert.notNull(processInstance, "流程实例不存在");
        FlowableHooks.getHooks(FlowableHooks.ProcessDeleteBeforeHook.class, processInstance.getProcessDefinitionKey()).forEach(hook -> hook.onProcessDeleteBefore(processInstance));
        runtimeService.deleteProcessInstance(procInstId, deleteReason);
        FlowableHooks.getHooks(FlowableHooks.ProcessDeleteAfterHook.class, processInstance.getProcessDefinitionKey()).forEach(hook -> hook.onProcessDeleteAfter(processInstance));
    }

    @PutMapping("suspend")
    @Operation(summary = "挂起流程实例")
    public void suspend(String id) {
        runtimeService.suspendProcessInstanceById(id);
    }

    @PutMapping("activate")
    @Operation(summary = "激活流程实例")
    public void activate(String id) {
        runtimeService.activateProcessInstanceById(id);
    }

    @GetMapping("hisTask")
    @Operation(summary = "查询流程实例历史任务")
    public List<FlowHisTask> hisTask(String procInstId) {
        return historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstId).orderByTaskCreateTime().asc().list()
                .stream().map(it -> {
                    FlowHisTask flowHisTask = new FlowHisTask();
                    flowHisTask.setInstance(it);
                    flowHisTask.setComments(taskService.getTaskComments(it.getId()));
                    return flowHisTask;
                }).collect(Collectors.toList());
    }

    @GetMapping("hisDiagram")
    @Operation(summary = "查询流程实例历史流程图")
    @SneakyThrows
    public String hisDiagram(String procInstId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        Assert.notNull(historicProcessInstance, "流程实例不存在");
        // 获取流程定义
        BpmnModel bpmnModel = FlowableUtil.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 获取当前正在进行的节点
        List<String> activeActivityIds = new ArrayList<>();
        if(historicProcessInstance.getEndTime() == null){
            activeActivityIds = runtimeService.getActiveActivityIds(procInstId);
        }
        // 生成流程图
        return FlowableUtil.generateThumbnailBase64(bpmnModel, "png", activeActivityIds, List.of());
    }


}
