package top.kthirty.flowable.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.flowable.listener.FlowableHooks;

@Component
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FlowableHelper {
    private final RuntimeService runtimeService;

    public void start(String processDefinitionKey,String businessKey) {
        Assert.notBlank(processDefinitionKey,"流程定义KEY不可为空");
        Assert.notBlank(businessKey,"业务流程KEY不可为空");
        Assert.isTrue(runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).count() == 0,"当前业务流程KEY已存在正在运行的流程，无法重复发起");

        Kv variables = Kv.init();
        // 执行前置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartBeforeHook.class, processDefinitionKey)
                .forEach(hook -> variables.putAll(ObjUtil.defaultIfNull(hook.handle(processDefinitionKey,businessKey), MapUtil.empty())));
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        // 执行流程实例名称钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessInstanceNameGenerator.class,processDefinitionKey)
                .forEach(it -> runtimeService.setProcessInstanceName(processInstance.getProcessInstanceId(), it.generate(processDefinitionKey,businessKey)));
        // 执行后置钩子
        FlowableHooks.getHooks(FlowableHooks.ProcessStartAfterHook.class,processDefinitionKey)
                .forEach(it -> it.handle(processDefinitionKey,businessKey,processInstance));
    }
}
