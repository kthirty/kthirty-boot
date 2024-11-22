package top.kthirty.flowable.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeansException;
import top.kthirty.core.tool.utils.SpringUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FlowableHooks {
    public interface BaseHook{ List<String> listenProcessDefinitionKey();}
    public interface ProcessStartBeforeHook extends BaseHook { Map<String, Object> handle(String processDefinitionKey, String businessKey);}
    public interface ProcessInstanceNameGenerator extends BaseHook {String generate(String processDefinitionKey, String businessKey);}
    public interface ProcessStartAfterHook extends BaseHook { void handle(String processDefinitionKey, String businessKey, ProcessInstance processInstance);}



    /**
     * 获取所有钩子
     * @param hookClass 钩子类型
     */
    public static <T extends BaseHook> Collection<T> getHooks(Class<T> hookClass, String processDefinitionKey){
        try{
            return SpringUtil.getBeansOfType(hookClass)
                    .values()
                    .stream()
                    .filter(hook -> CollUtil.contains(hook.listenProcessDefinitionKey(),processDefinitionKey))
                    .toList();
        }catch (BeansException e){
            return ListUtil.empty();
        }
    }
}
