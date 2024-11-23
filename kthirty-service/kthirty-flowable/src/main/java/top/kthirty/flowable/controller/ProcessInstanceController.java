package top.kthirty.flowable.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.tool.Func;
import top.kthirty.flowable.model.FlowProcessInstQuery;

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





}
