package top.kthirty.flowable.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.io.InputStreamProvider;
import org.flowable.common.engine.impl.util.io.StringStreamSource;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.persistence.entity.ModelEntityImpl;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.core.tool.utils.Base64Util;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.core.web.utils.WebUtil;
import top.kthirty.flowable.model.FlowModel;
import top.kthirty.flowable.model.FlowModelQuery;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description 模型控制器
 * @author KThirty
 * @since 2024/11/21 15:21
 */
@RestController
@RequestMapping("model")
@RequiredArgsConstructor
public class ModelController extends BaseController {
    private final RepositoryService repositoryService;
    private final ManagementService managementService;
    private final ProcessEngine processEngine;
    public static final String MODEL_JSON_FILE_NAME = "models.json";

    @GetMapping("page")
    @Operation(summary = "分页查询流程模型")
    public List<Model> page(FlowModelQuery req){
        ModelQuery query = repositoryService.createModelQuery();
        query.latestVersion();
        Func.doIf(StrUtil.isNotBlank(req.getKey()),() -> query.modelKey(req.getKey()));
        Func.doIf(StrUtil.isNotBlank(req.getCategory()),() -> query.modelCategory(req.getCategory()));
        Func.doIf(StrUtil.isNotBlank(req.getName()),() -> query.modelNameLike(req.getName()));
        Func.doIf(StrUtil.isNotBlank(req.getTenantId()), () -> query.modelTenantId(req.getTenantId()));
        Func.doIf(ObjUtil.isNotNull(req.getDeployed()) && req.getDeployed(), query::deployed);
        Func.doIf(ObjUtil.isNotNull(req.getDeployed()) && !req.getDeployed(), query::notDeployed);
        return query.listPage(req.getPageNumber() - 1, req.getPageSize());
    }

    @PostMapping("save")
    @Operation(summary = "保存流程模型")
    public void save(@RequestBody @Parameter(description = "模型信息") FlowModel model){
        repositoryService.saveModel(model);
        if(StrUtil.isNotBlank(model.getXml())){
            repositoryService.addModelEditorSource(model.getId(),model.getXml().getBytes(StandardCharsets.UTF_8));
        }
    }

    @GetMapping("get")
    @Operation(summary = "获取单个模型详情")
    @SneakyThrows
    public FlowModel get(@Parameter(description = "模型ID") String modelId
            , @Parameter(description = "是否查询xml") @RequestParam(required = false, defaultValue = "true") boolean queryXml
            , @Parameter(description = "是否查询缩略图") @RequestParam(required = false) boolean queryThumbnail){
        Model model = repositoryService.getModel(modelId);
        FlowModel flowModel = BeanUtil.copy(model, FlowModel.class);
        if(queryXml || queryThumbnail){
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
            if(ObjUtil.isNotEmpty(modelEditorSource)){
                flowModel.setXml(new String(modelEditorSource));
                // 查询缩略图
                if(queryThumbnail){
                    BpmnModel bpmnModel = new BpmnXMLConverter()
                            .convertToBpmnModel(() -> IoUtil.toStream(modelEditorSource), true, true, Charsets.UTF_8_NAME);
                    BufferedImage bufferedImage = processEngine.getProcessEngineConfiguration()
                            .getProcessDiagramGenerator()
                            .generatePngImage(bpmnModel, 1.0D);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", stream);
                    String imageBase64 = Base64.encode(stream.toByteArray());
                    flowModel.setThumbnail(imageBase64);
                }
            }
        }
        return flowModel;
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除模型")
    public void delete(@Parameter(description = "模型ID") String modelId){
        repositoryService.deleteModel(modelId);
    }

    @PutMapping("deploy")
    @Operation(summary = "模型部署")
    public Deployment deploy(@Parameter(description = "模型ID") String modelId){
        Model model = repositoryService.getModel(modelId);
        byte[] xmlByte = repositoryService.getModelEditorSource(modelId);
        Assert.notNull(model,"模型不存在");
        Assert.isTrue(ObjUtil.isNotEmpty(xmlByte),"模型XML有误");
        return repositoryService.createDeployment()
                .name(model.getName())
                .key(model.getKey())
                .category(model.getCategory())
                .addBytes("process.bpmn", xmlByte)
                .deploy();
    }

    @GetMapping("exportZip")
    @Operation(summary = "导出模型")
    public void exportZip(@Parameter(description = "模型ID") String modelIds){
        List<String> modelIdList = StrUtil.splitTrim(modelIds, ",");
        Assert.notEmpty(modelIdList,"模型ID不可为空");
        String inStr = modelIdList.stream().map(it -> "'" + it + "'").collect(Collectors.joining(","));
        List<Model> list = repositoryService.createNativeModelQuery()
                .sql(StrUtil.format("select * from {} where ID_ in ({})", managementService.getTableName(Model.class), inStr))
                .list();
        // 创建文件
        File dir = FileUtil.mkdir(FileUtil.createTempFile(null));
        FileUtil.rename(dir,"flowable-model-exp",true);
        // model列表
        File modelJson = FileUtil.file(dir, MODEL_JSON_FILE_NAME);
        FileUtil.writeUtf8String(JsonUtil.toJson(list),modelJson);
        // xml文件
        list.forEach(it -> {
            byte[] xmlByte = repositoryService.getModelEditorSource(it.getId());
            File xmlFile = FileUtil.file(dir, it.getId()+".xml");
            FileUtil.writeBytes(xmlByte,xmlFile);
        });
        // 压缩文件
        File zip = ZipUtil.zip(dir,CharsetUtil.CHARSET_UTF_8);
        WebUtil.renderFile(zip);
    }

    @PostMapping("importZip")
    @Operation(summary = "导入模型")
    @SneakyThrows
    public void importZip(MultipartFile[] files){
        for (MultipartFile multipartFile : files) {
            File zipFile = FileUtil.rename(FileUtil.createTempFile(null), IdUtil.fastSimpleUUID() + ".zip", true);
            FileUtil.writeFromStream(multipartFile.getInputStream(),zipFile);
            File dir = ZipUtil.unzip(zipFile);
            // 解析model json
            File modelJsonFile = FileUtil.file(dir, MODEL_JSON_FILE_NAME);
            String modelJson = FileUtil.readUtf8String(modelJsonFile);
            List<ModelEntityImpl> modelEntities = JsonUtil.parseArray(modelJson, ModelEntityImpl.class);
            Assert.notEmpty(modelEntities,"不存在模型");
            // 解析xml
            Objects.requireNonNull(modelEntities).forEach(it -> {
                File xmlFile = FileUtil.file(dir, it.getId()+".xml");
                String xmlStr = FileUtil.readUtf8String(xmlFile);
                Model model = ObjUtil.defaultIfNull(repositoryService.createModelQuery().modelKey(it.getKey()).singleResult(),repositoryService.newModel());
                model.setCategory(it.getCategory());
                model.setKey(it.getKey());
                model.setName(it.getName());
                model.setTenantId(it.getTenantId());
                model.setMetaInfo(it.getMetaInfo());
                // 保存模型
                repositoryService.saveModel(model);
                repositoryService.addModelEditorSource(model.getId(),xmlStr.getBytes(StandardCharsets.UTF_8));
            });
        }
    }
}
