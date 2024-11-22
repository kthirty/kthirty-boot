package top.kthirty.flowable.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.flowable.model.FlowProcessDefModel;
import top.kthirty.flowable.model.FlowProcessDefQuery;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @description 流程相关接口
 * @author KThirty
 * @since 2024/11/22 15:30
 */
@RestController
@RequestMapping("process")
@RequiredArgsConstructor
public class ProcessController extends BaseController {
    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;

    @GetMapping("def/page")
    @Operation(summary = "获取流程定义分页")
    public List<ProcessDefinition> defPage(FlowProcessDefQuery req){
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        Func.doIf(StrUtil.isNotBlank(req.getCategory()), () -> query.processDefinitionKeyLike(req.getCategory()));
        Func.doIf(StrUtil.isNotBlank(req.getKey()), () -> query.processDefinitionKeyLike(req.getKey()));
        Func.doIf(StrUtil.isNotBlank(req.getName()), () -> query.processDefinitionNameLike(req.getName()));
        Func.doIf(StrUtil.isNotBlank(req.getTenantId()), () -> query.processDefinitionTenantId(req.getTenantId()));
        Func.doIf(req.getActive() != null && req.getActive() , query::active);
        Func.doIf(req.getActive() != null && !req.getActive() , query::suspended);
        return query.latestVersion()
                .listPage(req.getPageNumber() - 1,req.getPageSize());
    }
    @PutMapping("def/active")
    @Operation(summary = "流程激活")
    public void devActive(String processDefId){
        repositoryService.activateProcessDefinitionById(processDefId);
    }
    @PutMapping("def/suspended")
    @Operation(summary = "流程激活")
    public void devSuspended(String processDefId){
        repositoryService.suspendProcessDefinitionById(processDefId);
    }

    @GetMapping("get")
    @Operation(summary = "获取单个流程定义详情")
    @SneakyThrows
    public FlowProcessDefModel get(@Parameter(description = "流程定义ID") String id
            , @Parameter(description = "是否查询xml") @RequestParam(required = false) boolean queryXml
            , @Parameter(description = "是否查询缩略图") @RequestParam(required = false) boolean queryThumbnail){
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(id);
        FlowProcessDefModel flowProcessDefModel = BeanUtil.copy(processDefinition, FlowProcessDefModel.class);
        if(queryXml || queryThumbnail){
            @Cleanup
            InputStream xmlStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),"process.bpmn");
            flowProcessDefModel.setXml(StrUtil.str(IoUtil.readBytes(xmlStream,false), Charsets.UTF_8));
            if(queryThumbnail){
                BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(() -> xmlStream, true, true, Charsets.UTF_8_NAME);
                BufferedImage bufferedImage = processEngine.getProcessEngineConfiguration()
                        .getProcessDiagramGenerator()
                        .generatePngImage(bpmnModel, 1.0D);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", stream);
                String imageBase64 = Base64.encode(stream.toByteArray());
                flowProcessDefModel.setThumbnail(imageBase64);
            }
        }
        return flowProcessDefModel;
    }

}
