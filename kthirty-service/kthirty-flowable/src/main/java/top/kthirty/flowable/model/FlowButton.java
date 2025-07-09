package top.kthirty.flowable.model;

import lombok.Data;
/**
 * @description 配置的按钮属性
 * @author KThirty
 * @since 2024/11/25 10:55
 */
@Data
public class FlowButton {
    /**
     * 按钮ID，唯一标识，并无其他作用
     */
    private String code;
    /**
     * 按钮名称
     */
    private String name;
    /**
     * 按钮对应的流程结果代码（用于流程结果判断）
     */
    private String resultCode;
    /**
     * 流程按钮的详细描述
     */
    private String description;
    /**
     * 是否必须要审批意见
     */
    private Boolean commentRequired;
}
