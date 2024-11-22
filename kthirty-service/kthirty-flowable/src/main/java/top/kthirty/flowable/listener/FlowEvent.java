package top.kthirty.flowable.listener;

import lombok.AllArgsConstructor;
/**
 * 流程事件
 * @author Kthirty
 * @since 2024/11/22
 */
@AllArgsConstructor
public enum FlowEvent {
    PROCESS_STARTED("PROCESS_STARTED", "流程启动"),
    PROCESS_COMPLETED("PROCESS_COMPLETED", "流程完成"),
    PROCESS_DELETED("PROCESS_DELETED", "流程删除"),
    TASK_CREATED("TASK_CREATED", "任务创建"),
    TASK_ASSIGNED("TASK_ASSIGNED", "任务指派"),
    TASK_COMPLETED("TASK_COMPLETED", "任务完成");

    private final String event;
    private final String description;
}
