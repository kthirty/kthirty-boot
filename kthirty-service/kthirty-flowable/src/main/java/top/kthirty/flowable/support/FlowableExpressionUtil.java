package top.kthirty.flowable.support;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.Execution;

/**
 * @description 流程脚本工具
 * @author KThirty
 * @since 2024/12/1 13:32
 */
@Slf4j
public class FlowableExpressionUtil {
    public String queryStarterDeptRole(Execution execution,String roleName) {
        String processInstanceId = execution.getProcessInstanceId();
        log.info("processInstanceId:{}",processInstanceId);
        return "TG:"+roleName;
    }
}
