package top.kthirty.flowable.model;

import lombok.Data;
/**
 * @description 配置的按钮属性
 * @author KThirty
 * @since 2024/11/25 10:55
 */
@Data
public class FlowButton {
    private String code;
    private String name;
    private String resultCode;
    private String description;
    private Boolean commentRequired;
}
