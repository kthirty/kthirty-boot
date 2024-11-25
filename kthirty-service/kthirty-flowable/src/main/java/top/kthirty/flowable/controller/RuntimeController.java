package top.kthirty.flowable.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.*;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.flowable.model.FlowButton;
import top.kthirty.flowable.model.FlowTask;
import top.kthirty.flowable.model.FlowTodoQuery;
import top.kthirty.flowable.model.TaskCompleteReq;
import top.kthirty.flowable.util.FlowableHelper;
import top.kthirty.flowable.util.FlowableUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ru")
@RequiredArgsConstructor
@Tag(name = "运行时相关接口")
public class RuntimeController {
    private final FlowableHelper flowableHelper;
    private final TaskService taskService;
    private final RuntimeService runtimeService;

    @PostMapping("start")
    @Operation(summary = "启动流程实例")
    public void start(@Parameter(description = "流程定义KEY") String processDefinitionKey, @Parameter(description = "业务表ID") String businessKey) {
        flowableHelper.start(processDefinitionKey, businessKey);
    }

    @GetMapping("todo")
    @Operation(summary = "待办任务")
    public Page<FlowTask> todo(FlowTodoQuery req) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        Func.doNotBlank(req.getProcessDefKey(), () -> taskQuery.processDefinitionKey(req.getProcessDefKey()));
        Func.doNotBlank(req.getProcessDefName(), () -> taskQuery.processDefinitionNameLike(req.getProcessDefName()));
        Func.doNotBlank(req.getProcessInstId(), () -> taskQuery.processInstanceIdIn(StrUtil.splitTrim(req.getProcessInstId(), StringPool.COMMA)));
        Func.doNotBlank(req.getTaskName(), () -> taskQuery.taskNameLike(req.getTaskName()));
        Func.doNotBlank(req.getTaskDefKey(), () -> taskQuery.taskDefinitionKeys(StrUtil.splitTrim(req.getTaskDefKey(), StringPool.COMMA)));
        Func.doIf(req.getActive() != null && req.getActive(), taskQuery::active);
        Func.doIf(req.getActive() != null && !req.getActive(), taskQuery::suspended);
        Func.doNotBlank(req.getBusinessKey(), () -> taskQuery.processInstanceBusinessKey(req.getBusinessKey()));
        Func.doNotNull(req.getTaskCreateTimeStart(), () -> taskQuery.taskCreatedAfter(req.getTaskCreateTimeStart()));
        Func.doNotNull(req.getTaskCreateTimeEnd(), () -> taskQuery.taskCreatedBefore(req.getTaskCreateTimeEnd()));
        Func.doNotBlank(req.getProcessInstName(), () -> {
            List<String> procInstIds = CollUtil.defaultIfEmpty(runtimeService.createProcessInstanceQuery()
                    .processInstanceNameLike(req.getProcessInstName())
                    .list()
                    .stream()
                    .map(Execution::getProcessInstanceId)
                    .toList(), CollUtil.toList(Constant.NOT_FOUND));
            taskQuery.processInstanceIdIn(procInstIds);
        });
        if(!SecureUtil.isSuperAdmin()){
            // 任务人筛选处理
            // 1. 所有拥有的角色 2. 拥有的部门:职位 3. 拥有的部门:角色
            List<String> groupCodes = CollUtil.unionAll(CollUtil.toList(Constant.NOT_FOUND),SecureUtil.getRoles(), SecureUtil.getIdentityCodes());
            SecureUtil.getDeptCodes().forEach(deptCode -> SecureUtil.getRoles().forEach(roleCode -> groupCodes.add(StrUtil.join(StringPool.COLON, deptCode, roleCode))));
            taskQuery.taskCandidateOrAssigned(SecureUtil.getUsername())
                    .or()
                    .taskCandidateGroupIn(groupCodes);
            // 签收 : 未签收或者本人签收的任务
            taskQuery.taskUnassigned().or().taskClaimedBy(SecureUtil.getUsername());
        }
        // 排序
        taskQuery.orderByTaskCreateTime().desc();
        // 查询任务
        List<Task> tasks = taskQuery.listPage(req.getFirstResult(), req.getPageSize());
        if (CollUtil.isEmpty(tasks)) {
            return req.getPage(0, ListUtil.empty());
        }
        // 查询流程实例信息
        Map<String, ProcessInstance> procInstMap = new HashMap<>(tasks.size());
        runtimeService.createProcessInstanceQuery()
                .processInstanceIds(tasks.stream().map(TaskInfo::getProcessInstanceId).collect(Collectors.toSet()))
                .list()
                .forEach(it -> procInstMap.put(it.getId(), it));
        return req.getPage(taskQuery.count(), tasks.stream().map(it -> new FlowTask(it,procInstMap.get(it.getProcessInstanceId()))).toList());
    }

    @PutMapping("complete")
    @Operation(summary = "任务办理")
    public void complete(@RequestBody @Valid @Parameter(description = "办理信息") TaskCompleteReq req) {
        flowableHelper.complete(req);
    }
    @GetMapping("handleButtons")
    @Operation(summary = "查询处理按钮")
    public List<FlowButton> handleButtons(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task,"任务不存在");
        BpmnModel bpmnModel = FlowableUtil.getBpmnModel(task.getProcessDefinitionId());
        Activity flowElement = (Activity) bpmnModel.getFlowElement(task.getTaskDefinitionId());
        return FlowableUtil.getHandleButtons(flowElement);
    }

    @PutMapping("claim")
    @Operation(summary = "任务签收")
    public void claim(@Parameter(description = "任务ID") String taskId) {
        taskService.claim(taskId, SecureUtil.getUsername());
    }
    @PutMapping("unclaim")
    @Operation(summary = "任务退签收")
    public void unclaim(@Parameter(description = "任务ID") String taskId) {
        if(!SecureUtil.isSuperAdmin()){
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Assert.isTrue(task.getClaimedBy().equals(SecureUtil.getUsername()), "只能退签收本人签收的任务");
        }
        taskService.unclaim(taskId);
    }

    @PutMapping("activate")
    @Operation(summary = "任务激活")
    public void complete(@Parameter(description = "任务ID") String taskId) {
        taskService.activateTask(taskId, SecureUtil.getUsername());
    }
    @PutMapping("suspend")
    @Operation(summary = "任务挂起")
    public void suspend(@Parameter(description = "任务ID") String taskId) {
        taskService.suspendTask(taskId, SecureUtil.getUsername());
    }
}
