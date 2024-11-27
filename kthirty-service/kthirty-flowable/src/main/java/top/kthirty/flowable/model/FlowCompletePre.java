package top.kthirty.flowable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description 流程完成前置参数
 * @author KThirty
 * @since 2024/11/27 18:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowCompletePre {
    private List<FlowButton> handleButtons;
    private String formKey;
}
