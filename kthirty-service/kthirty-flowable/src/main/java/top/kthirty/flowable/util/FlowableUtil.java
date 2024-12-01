package top.kthirty.flowable.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.task.api.Task;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.core.tool.utils.IoUtil;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.flowable.model.FlowButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author KThirty
 * @description
 * @since 2024/11/25 11:02
 */
public class FlowableUtil {
    /**
     * 获取下一个节点（并行网关情况下返回第一个）
     *
     * @param bpmnModel   流程定义
     * @param currentNode 当前节点
     * @param variables   流程变量
     * @return FlowElement 节点定义
     */
    public static FlowElement getNextNode(BpmnModel bpmnModel, FlowNode currentNode, Map<String, Object> variables) {
        List<FlowElement> nextNodes = getNextNodes(bpmnModel, currentNode, variables);
        return CollUtil.isEmpty(nextNodes) ? null : nextNodes.get(0);
    }

    /**
     * 获取下一个节点
     *
     * @param bpmnModel   流程定义
     * @param currentNode 当前节点
     * @param variables   流程变量
     * @return FlowElement 节点定义
     */
    public static List<FlowElement> getNextNodes(BpmnModel bpmnModel, FlowNode currentNode, Map<String, Object> variables) {
        SpringProcessEngineConfiguration springProcessEngineConfiguration = SpringUtil.getBean(SpringProcessEngineConfiguration.class);
        ExpressionManager expressionManager = springProcessEngineConfiguration.getExpressionManager();
        // 可能产生多个节点
        if (currentNode instanceof ParallelGateway
                || currentNode instanceof InclusiveGateway
                || currentNode instanceof ComplexGateway) {
            return currentNode.getOutgoingFlows()
                    .stream()
                    .filter(sequenceFlow ->
                            Func.isNull(sequenceFlow.getConditionExpression())
                                    || Convert.toBool(expressionManager
                                    .createExpression(sequenceFlow.getConditionExpression())
                                    .getValue(new VariableContainerWrapper(variables)), false))
                    .map(SequenceFlow::getTargetFlowElement)
                    .toList();
        } else {
            // 只会产生单一节点
            FlowElement flowElement = currentNode.getOutgoingFlows()
                    .stream()
                    .filter(sequenceFlow ->
                            Func.isNull(sequenceFlow.getConditionExpression())
                                    || Convert.toBool(expressionManager
                                    .createExpression(sequenceFlow.getConditionExpression())
                                    .getValue(new VariableContainerWrapper(variables)), false))
                    .map(SequenceFlow::getTargetFlowElement)
                    .findFirst()
                    .orElseGet(() -> {
                        if (currentNode instanceof Activity) {
                            String defaultFlow = ((Activity) currentNode).getDefaultFlow();
                            if (StrUtil.isNotBlank(defaultFlow)) {
                                return bpmnModel.getFlowElement(defaultFlow);
                            }
                        }
                        return null;
                    });
            return flowElement != null ? List.of(flowElement) : List.of();
        }
    }

    /**
     * 获取流程的下一个任务节点
     *
     * @param taskId    任务ID
     * @param variables 流程变量
     * @return FlowElement 节点定义
     */
    public static FlowElement getNextNode(String taskId, Map<String, Object> variables) {
        ProcessEngine processEngine = SpringUtil.getBean(ProcessEngine.class);
        Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "任务不存在");
        // 读取流程实例变量
        Kv procInstVars = Kv.init(variables);
        processEngine.getRuntimeService().createVariableInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .list()
                .forEach(it -> procInstVars.put(it.getName(), it.getValue()));
        // 获取定义
        BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(task.getProcessDefinitionId());
        FlowNode currentNode = (FlowNode) bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        return getNextNode(bpmnModel, currentNode, procInstVars);
    }

    /**
     * 获取BpmnModel
     *
     * @param processDefinitionId 流程定义ID
     * @return BpmnModel
     */
    @SneakyThrows
    public static BpmnModel getBpmnModel(String processDefinitionId) {
        ProcessEngine processEngine = SpringUtil.getBean(ProcessEngine.class);
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        return new BpmnXMLConverter()
                .convertToBpmnModel(() -> repositoryService.getResourceAsStream(
                                processDefinition.getDeploymentId(), "process.bpmn")
                        , true
                        , true
                        , Charsets.UTF_8_NAME);
    }
    public static BpmnModel getBpmnModelByXml(String xml){
        return new BpmnXMLConverter()
                .convertToBpmnModel(() -> new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
                        , true
                        , true
                        , Charsets.UTF_8_NAME);
    }

    /**
     * 获取节点的处理按钮
     *
     * @param activity UserTask 节点
     * @return 处理按钮
     */
    public static List<FlowButton> getHandleButtons(Activity activity) {
        return CollUtil.emptyIfNull(activity.getExtensionElements().get("handleButton"))
                .stream()
                .filter(Func::notNull)
                .map(it -> {
                    Kv attr = Kv.init();
                    it.getAttributes().forEach((key, values) -> values.forEach(val -> attr.set(val.getName(), val.getValue())));
                    return attr.toBean(FlowButton.class);
                }).collect(Collectors.toList());
    }
    public static String getFormKey(Activity activity){
        return (activity instanceof UserTask userTask) ? userTask.getFormKey() : null;
    }

    /**
     * 获取流程实例名称表达式
     *
     * @param process 主流程
     * @return 流程实例名称表达式
     */
    public static String getProcessNameExp(Process process) {
        List<ExtensionElement> processNameExpList = process.getExtensionElements().get("processNameExp");
        if (CollUtil.isEmpty(processNameExpList)) {
            return null;
        }
        ExtensionElement extensionElement = processNameExpList.get(0);
        if (StrUtil.isNotBlank(extensionElement.getElementText())) {
            return null;
        }
        return extensionElement.getElementText();
    }

    public static String getProcessName(ProcessDefinition processDefinition, String businessKey, Map<String,Object> variables) {
        String processDefinitionKey = processDefinition.getKey();
        String processDefinitionName = processDefinition.getName();
        String processDefinitionId = processDefinition.getId();
        BpmnModel bpmnModel = FlowableUtil.getBpmnModel(processDefinitionId);
        String processNameExp = FlowableUtil.getProcessNameExp(bpmnModel.getMainProcess());
        if(StrUtil.isNotBlank(processNameExp)){
            Kv tempVar = Kv.init().setAll(variables).set("processDefinitionKey", processDefinitionKey)
                    .set("processDefinitionName", processDefinitionName)
                    .set("businessKey", businessKey);
            String processNameByExp = StrUtil.toString(CommandContextUtil.getProcessEngineConfiguration()
                    .getExpressionManager()
                    .createExpression(processNameExp)
                    .getValue(new VariableContainerWrapper(tempVar)));
            if(StrUtil.isNotBlank(processNameByExp)){
                return processNameByExp;
            }
        }
        // 执行流程实例名称钩子
        List<FlowableHooks.ProcessInstanceNameGenerator> hooks = FlowableHooks.getHooks(FlowableHooks.ProcessInstanceNameGenerator.class, processDefinitionKey);
        if(CollUtil.isNotEmpty(hooks)){
            String hookGenProcessInstanceName = hooks.get(0).generateProcessInstanceName(processDefinitionKey, processDefinitionName, businessKey);
            return StrUtil.blankToDefault(hookGenProcessInstanceName,processDefinitionName);
        }
        return processDefinitionName;
    }

    /**
     * 生成流程图
     * @param bpmnModel 流程模型
     * @param imageType 图片类型
     * @param highLightedActivities 高亮节点
     * @param highLightedFlows 高亮线
     * @return 图片流
     */
    @SneakyThrows
    public static InputStream generateThumbnail(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows){
        ProcessEngineConfiguration processEngineConfiguration = SpringUtil.getBean(ProcessEngineConfiguration.class);
        return processEngineConfiguration.getProcessDiagramGenerator()
                .generateDiagram(bpmnModel
                        , imageType
                        , highLightedActivities
                        , highLightedFlows
                        , processEngineConfiguration.getActivityFontName()
                        , processEngineConfiguration.getLabelFontName()
                        , processEngineConfiguration.getAnnotationFontName()
                        , processEngineConfiguration.getClassLoader()
                        , 1.0
                        , true);
    }
    @SneakyThrows
    public static InputStream generateThumbnail(BpmnModel bpmnModel, String imageType){
        return generateThumbnail(bpmnModel, imageType, List.of(), List.of());
    }

    @SneakyThrows
    public static String generateThumbnailBase64(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities, List<String> highLightedFlows){
        @Cleanup
        InputStream inputStream = generateThumbnail(bpmnModel, imageType, highLightedActivities, highLightedFlows);
        return "data:image/png;base64,"+Base64.encode(inputStream);
    }
    @SneakyThrows
    public static String generateThumbnailBase64(BpmnModel bpmnModel, String imageType){
        return generateThumbnailBase64(bpmnModel, imageType, List.of(), List.of());
    }

    /**
     * 获取流程的xml
     * @param deploymentId 部署id
     * @return xml
     */
    public static String getXml(String deploymentId){
        return IoUtil.toString(SpringUtil.getBean(RepositoryService.class).getResourceAsStream(deploymentId, "process.bpmn"),Charsets.UTF_8);
    }

    public static void main(String[] args) {
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(() ->
                FileUtil.getInputStream(FileUtil.file("D:\\System\\Download\\diagram.bpmn")
                ), true, true, Charsets.UTF_8_NAME);
        Activity flowElement = (Activity) bpmnModel.getFlowElement("task1");
        getHandleButtons(flowElement).forEach(System.out::println);
        String processNameExp = getProcessNameExp(bpmnModel.getMainProcess());
    }
}
