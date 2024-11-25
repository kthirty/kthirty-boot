package top.kthirty.flowable.support;

import lombok.Setter;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
/**
 * <p>
 * 数据库字段修改工具
 * </p>
 *
 * @author KThirty
 * @since 2024/11/25
 */
@Setter
public class TableFieldModifier implements FlowableEventListener {
    private String tableName;
    private String tableColumn;
    private String columnValue;
    @Override
    public void onEvent(FlowableEvent event) {
        // 处理监听到的事件
        System.out.println("Event detected: " + event.getType());
        System.out.println("Table: " + tableName);
        System.out.println("Column: " + tableColumn);
        System.out.println("Value: " + columnValue);
        // TODO 实际业务逻辑
    }

    @Override
    public boolean isFailOnException() {return true;}
    @Override
    public boolean isFireOnTransactionLifecycleEvent() {return true;}
    @Override
    public String getOnTransaction() {return "COMMITTED";}
}
