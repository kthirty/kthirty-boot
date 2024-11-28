package top.kthirty.flowable.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.BeansException;
import org.springframework.core.Ordered;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.flowable.model.TaskCompleteReq;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.util.ClassUtil.getClassName;
/**
 * @description 流程钩子处理
 * @author KThirty
 * @since 2024/11/26 9:09
 */
@Slf4j
public class FlowableHooks {
    public interface BaseHook{ List<String> listenProcessDefinitionKey();
    default int getOrder(){return Ordered.LOWEST_PRECEDENCE;}
    }
    public interface ProcessStartBeforeHook extends BaseHook { Map<String, Object> onProcessStartBefore(String processDefinitionKey, String businessKey);}
    public interface ProcessInstanceNameGenerator extends BaseHook {
        String generateProcessInstanceName(String processDefinitionKey,String processDefinitionName, String businessKey);
    }
    public interface ProcessStartAfterHook extends BaseHook { void onProcessStartAfter(String processDefinitionKey, String businessKey, ProcessInstance processInstance);}
    public interface TaskCompleteBeforeHook extends BaseHook {
        /**
         * @param processInstance 流程实例
         * @param task 任务信息
         * @param req 办理请求信息
         * @return 流程变量（作为变量存入流程）
         */
        Map<String, Object> onTaskCompleteBefore(ProcessInstance processInstance, Task task, TaskCompleteReq req);
    }

    /**
     * 任务办理后
     */
    public interface TaskCompleteAfterHook extends BaseHook {
        /**
         * @param processInstance 流程实例
         * @param task 任务信息
         * @param req 办理请求信息
         * @return 流程变量（作为变量存入流程）
         */
        void onTaskCompleteAfter(ProcessInstance processInstance, Task task, TaskCompleteReq req,Map<String,Object> variables);
    }

    /**
     * 任务创建前
     */
    public interface TaskCreateBeforeHook extends BaseHook {
        void onTaskCreateBefore(ProcessInstance processInstance, FlowElement flowElement, Map<String,Object> variables);
    }

    /**
     * 流程结束前置，在最后一个节点处理完成之前运行
     */
    public interface ProcessCompleteBeforeHook extends BaseHook {
        void onProcessCompleteBefore(ProcessInstance processInstance);
    }

    /**
     * flowable 引擎原生监听器
     */
    public interface NativeEventHook extends BaseHook {
        /**
         * 需要监听的事件，为空监听所有
         */
        default List<FlowableEngineEventType> listenEventTypes(){return null;}
        void onNativeEvent(FlowableEngineEventType eventType, FlowableEngineEvent flowableEngineEvent);
    }

    /**
     * 流程删除前
     */
    public interface ProcessDeleteBeforeHook extends BaseHook{
        void onProcessDeleteBefore(ProcessInstance processInstance);
    }

    /**
     * 流程删除前后
     */
    public interface ProcessDeleteAfterHook extends BaseHook{
        void onProcessDeleteAfter(ProcessInstance processInstance);
    }


    /**
     * 获取所有钩子
     * @param hookClass 钩子类型
     */
    public static <T extends BaseHook> List<T> getHooks(Class<T> hookClass, String processDefinitionKey){
        try{
            List<T> hooks = SpringUtil.getBeansOfType(hookClass)
                    .values()
                    .stream()
                    .filter(hook -> CollUtil.contains(hook.listenProcessDefinitionKey(), processDefinitionKey))
                    .sorted(Comparator.comparingInt(BaseHook::getOrder))
                    .toList();
            log.info("获取到 {} 个 {} 钩子 {}", hooks.size()
                    , getClassName(hookClass,false)
                    , hooks.stream().map(it -> getClassName(it,false)).collect(Collectors.joining("\n")));
            return hooks;
        }catch (BeansException e){
            return ListUtil.empty();
        }
    }
}
