package top.kthirty.flowable.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.tool.Func;
import top.kthirty.flowable.model.FlowProcessInstQuery;
import top.kthirty.flowable.util.FlowableHelper;
import top.kthirty.flowable.util.FlowableUtil;

import java.io.InputStream;
import java.util.List;

/**
 * @description 流程实例控制器
 * @author KThirty
 * @since 2024/11/22 16:39
 */
@RestController
@RequestMapping("pi")
@RequiredArgsConstructor
@Tag(name = "流程实例")
public class ProcessInstanceController {
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final ProcessEngine processEngine;
    private final RepositoryService repositoryService;
    private final FlowableHelper flowableHelper;

    @GetMapping("page")
    @Operation(summary = "分页查询流程实例")
    public Page<HistoricProcessInstance> page(FlowProcessInstQuery req){
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        Func.doNotBlank(req.getDefCategory(),() -> query.processDefinitionCategory(req.getDefCategory()));
        Func.doNotBlank(req.getDefKey(),() -> query.processDefinitionKeyLike(req.getDefKey()));
        Func.doNotBlank(req.getDefName(),() -> query.processDefinitionNameLike(req.getDefName()));
        Func.doNotBlank(req.getInstName(),() -> query.processInstanceNameLike(req.getInstName()));
        Func.doIf(req.getFinished() != null && req.getFinished(), query::finished);
        Func.doIf(req.getFinished() != null && !req.getFinished(), query::unfinished);
        Func.doIf(req.getDeleted() != null && req.getDeleted(), query::deleted);
        Func.doIf(req.getDeleted() != null && !req.getDeleted(), query::notDeleted);
        query.orderByProcessInstanceStartTime().desc();
        return req.getPage(query.count(),query.listPage(req.getPageNumber() - 1,req.getPageSize()));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除流程实例")
    public void delete(String procInstId,String deleteReason){
        runtimeService.deleteProcessInstance(procInstId,deleteReason);
    }

    @PutMapping("suspend")
    @Operation(summary = "挂起流程实例")
    public void suspend(String procInstId){
        runtimeService.suspendProcessInstanceById(procInstId);
    }

    @PutMapping("activate")
    @Operation(summary = "激活流程实例")
    public void activate(String procInstId){
        runtimeService.activateProcessInstanceById(procInstId);
    }


    @GetMapping("hisTask")
    @Operation(summary = "查询流程实例历史任务")
    public List<HistoricTaskInstance> hisTask(String procInstId){
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId)
                .orderByTaskCreateTime().asc()
                .list();
    }
    @GetMapping("hisDiagram")
    @Operation(summary = "查询流程实例历史流程图")
    public String hisDiagram(HttpServletResponse response, String procInstId)  {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        Assert.notNull(historicProcessInstance,"流程实例不存在");
        // 获取流程定义
        BpmnModel bpmnModel = FlowableUtil.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 获取当前正在进行的节点
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(procInstId);
        // 生成流程图
        @Cleanup
        InputStream inputStream = processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(bpmnModel, "png", activeActivityIds, List.of(), true);
        return Base64.encode(inputStream);
    }


}
