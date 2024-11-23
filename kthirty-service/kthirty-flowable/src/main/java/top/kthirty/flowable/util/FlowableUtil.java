package top.kthirty.flowable.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.el.VariableContainerWrapper;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.api.Task;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.SpringUtil;

import java.util.List;
import java.util.Map;

public class FlowableUtil {
    /**
     * 获取下一个节点（并行网关情况下返回第一个）
     * @param bpmnModel 流程定义
     * @param currentNode 当前节点
     * @param variables 流程变量
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
        // 可能产生多个节点
        if (currentNode instanceof ParallelGateway
                || currentNode instanceof InclusiveGateway
                || currentNode instanceof ComplexGateway) {
            return currentNode.getOutgoingFlows()
                    .stream()
                    .filter(sequenceFlow ->
                            Func.isNull(sequenceFlow.getConditionExpression())
                                    || Convert.toBool(CommandContextUtil.getProcessEngineConfiguration()
                                    .getExpressionManager()
                                    .createExpression(sequenceFlow.getConditionExpression())
                                    .getValue(new VariableContainerWrapper(variables)), false))
                    .map(SequenceFlow::getTargetFlowElement)
                    .toList();
        }else{
            // 只会产生单一节点
            FlowElement flowElement = currentNode.getOutgoingFlows()
                    .stream()
                    .filter(sequenceFlow ->
                            Func.isNull(sequenceFlow.getConditionExpression())
                                    || Convert.toBool(CommandContextUtil.getProcessEngineConfiguration()
                                    .getExpressionManager()
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
}
