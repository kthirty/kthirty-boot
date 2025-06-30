package top.kthirty.flowable.controller;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.tool.Func;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.flowable.model.FlowProcessDefModel;
import top.kthirty.flowable.model.FlowProcessDefQuery;
import top.kthirty.flowable.util.FlowableUtil;

import java.util.List;

/**
 * @description 流程相关接口
 * @author KThirty
 * @since 2024/11/22 15:30
 */
@RestController
@RequestMapping("flw/process/definition")
@RequiredArgsConstructor
@Tag(name = "流程定义")
public class ProcessDefinitionController extends BaseController {
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    @GetMapping("page")
    @Operation(summary = "获取流程定义分页")
    public Page<FlowProcessDefModel> defPage(FlowProcessDefQuery req){
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        Func.doIf(StrUtil.isNotBlank(req.getCategory()), () -> query.processDefinitionKeyLike(req.getCategory()));
        Func.doIf(StrUtil.isNotBlank(req.getKey()), () -> query.processDefinitionKeyLike(req.getKey()));
        Func.doIf(StrUtil.isNotBlank(req.getName()), () -> query.processDefinitionNameLike(req.getName()));
        Func.doIf(req.getActive() != null && req.getActive() , query::active);
        Func.doIf(req.getActive() != null && !req.getActive() , query::suspended);
        query.latestVersion();
        List<FlowProcessDefModel> list = query.listPage(req.getFirstResult(), req.getPageSize())
                .stream()
                .map(FlowProcessDefModel::new)
                .toList();
        return req.getPage(query.count(),list);
    }
    @PutMapping("active")
    @Operation(summary = "流程激活")
    public void active(String processDefId){
        repositoryService.activateProcessDefinitionById(processDefId);
    }
    @PutMapping("suspended")
    @Operation(summary = "流程激活")
    public void suspended(String processDefId){
        repositoryService.suspendProcessDefinitionById(processDefId);
    }

    @GetMapping("get")
    @Operation(summary = "获取单个流程定义详情")
    @SneakyThrows
    public FlowProcessDefModel get(@Parameter(description = "流程定义ID") String id
            , @Parameter(description = "是否查询xml") @RequestParam(required = false) boolean queryXml
            , @Parameter(description = "是否查询缩略图") @RequestParam(required = false) boolean queryThumbnail){
        FlowProcessDefModel flowProcessDefModel = new FlowProcessDefModel(repositoryService.getProcessDefinition(id));
        if(queryXml){
            flowProcessDefModel.setXml(FlowableUtil.getXml(flowProcessDefModel.getDeploymentId()));
        }
        if(queryThumbnail){
            BpmnModel bpmnModel = FlowableUtil.getBpmnModel(flowProcessDefModel.getId());
            flowProcessDefModel.setThumbnail(FlowableUtil.generateThumbnailBase64(bpmnModel, "png"));
        }
        return flowProcessDefModel;
    }

}
