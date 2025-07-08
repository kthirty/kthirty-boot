package top.kthirty.flowable.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.flowable.model.FlowCompletePre;
import top.kthirty.flowable.model.FlowTask;
import top.kthirty.flowable.model.FlowTaskQuery;
import top.kthirty.flowable.model.TaskCompleteReq;
import top.kthirty.flowable.util.FlowableHelper;
import top.kthirty.flowable.util.FlowableUtil;

import java.util.List;

/**
 * @description 运行时相关接口
 * @author KThirty
 * @since 2024/11/26 15:44
 */
@RestController
@RequestMapping("flw/task")
@RequiredArgsConstructor
@Tag(name = "运行时相关接口")
@Transactional
public class TaskController extends BaseController {
    private final FlowableHelper flowableHelper;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    @PostMapping("start")
    @Operation(summary = "启动流程实例")
    public void start(@Parameter(description = "流程定义KEY") String processDefinitionKey, @Parameter(description = "业务表ID") String businessKey) {
        flowableHelper.start(processDefinitionKey, businessKey);
    }

    @GetMapping("done")
    @Operation(summary = "已办任务")
    public Page<FlowTask> done(FlowTaskQuery req) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery();
        req.handleTaskQuery(taskQuery);
        if(SecureUtil.isNotSuperAdmin()){
            taskQuery.taskCompletedBy(SecureUtil.getUsername());
        }
        List<FlowTask> flowTasks = taskQuery.orderByHistoricTaskInstanceEndTime().desc()
                .listPage(req.getFirstResult(), req.getPageSize())
                .stream()
                .map(FlowTask::new)
                .toList();
        return req.getPage(taskQuery.count(), FlowTask.fullProcessInstanceInfo(flowTasks));
    }
    @GetMapping("todo")
    @Operation(summary = "待办任务")
    public Page<FlowTask> todo(FlowTaskQuery req) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        req.handleTaskQuery(taskQuery);
        if(SecureUtil.isNotSuperAdmin()){
            // 任务人筛选处理
            // 1. 所有拥有的角色 2. 拥有的部门:职位 3. 拥有的部门:角色
            List<String> groupCodes = CollUtil.unionAll(CollUtil.toList(Constant.NOT_FOUND),SecureUtil.getRoles(), SecureUtil.getIdentityCodes());
            ObjUtil.defaultIfNull(SecureUtil.getDeptCodes(),List.of()).forEach(deptCode -> SecureUtil.getRoles().forEach(roleCode -> groupCodes.add(StrUtil.join(StringPool.COLON, deptCode, roleCode))));
            taskQuery.or().taskCandidateOrAssigned(SecureUtil.getUsername()).taskCandidateGroupIn(groupCodes).endOr();
        }
        // 排序
        List<FlowTask> flowTasks = taskQuery.orderByTaskCreateTime().desc()
                .listPage(req.getFirstResult(), req.getPageSize())
                .stream()
                .map(FlowTask::new)
                .toList();
        return req.getPage(taskQuery.count(), FlowTask.fullProcessInstanceInfo(flowTasks));
    }

    @PutMapping("complete")
    @Operation(summary = "任务办理")
    public void complete(@RequestBody @Valid @Parameter(description = "办理信息") TaskCompleteReq req) {
        flowableHelper.complete(req);
    }
    @GetMapping("completePre")
    @Operation(summary = "任务办理前置信息")
    public FlowCompletePre completePre(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task,"任务不存在");
        BpmnModel bpmnModel = FlowableUtil.getBpmnModel(task.getProcessDefinitionId());
        UserTask flowElement = (UserTask) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        return FlowCompletePre.builder()
                .formKey(FlowableUtil.getFormKey(flowElement))
                .handleButtons(FlowableUtil.getHandleButtons(flowElement))
                .build();
    }


    @PutMapping("claim")
    @Operation(summary = "任务签收")
    public void claim(@Parameter(description = "任务ID") String taskId) {
        taskService.claim(taskId, SecureUtil.getUsername());
    }
    @PutMapping("unclaim")
    @Operation(summary = "任务退签收")
    public void unclaim(@Parameter(description = "任务ID") String taskId) {
        if(SecureUtil.isNotSuperAdmin()){
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            Assert.isTrue(task.getClaimedBy().equals(SecureUtil.getUsername()), "只能退签收本人签收的任务");
        }
        taskService.unclaim(taskId);
    }

    @PutMapping("activate")
    @Operation(summary = "任务激活")
    public void activate(@Parameter(description = "任务ID") String taskId) {
        Authentication.setAuthenticatedUserId(SecureUtil.getUsername());
        taskService.activateTask(taskId, SecureUtil.getUsername());
    }
    @PutMapping("suspend")
    @Operation(summary = "任务挂起")
    public void suspend(@Parameter(description = "任务ID") String taskId) {
        Authentication.setAuthenticatedUserId(SecureUtil.getUsername());
        taskService.suspendTask(taskId, SecureUtil.getUsername());
    }
}
