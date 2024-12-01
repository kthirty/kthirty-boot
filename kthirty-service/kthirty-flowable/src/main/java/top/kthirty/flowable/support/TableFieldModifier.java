package top.kthirty.flowable.support;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.event.FlowableEventSupport;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 数据库字段修改工具
 * </p>
 *
 * @author KThirty
 * @since 2024/11/25
 */
@Slf4j
@Component
public class TableFieldModifier implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        String tableName = Convert.toStr(execution.getVariable("tableName"));
        String tableColumn = Convert.toStr(execution.getVariable("tableColumn"));
        String columnValue = Convert.toStr(execution.getVariable("columnValue"));
        log.info("修改表[{}]字段[{}]为[{}]", tableName, tableColumn, columnValue);
        throw new RuntimeException("测试监听异常");
    }
}
