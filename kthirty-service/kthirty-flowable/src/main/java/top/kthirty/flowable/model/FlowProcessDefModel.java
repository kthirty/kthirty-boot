package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
/**
 * @description 流程定义信息
 * @author KThirty
 * @since 2024/11/22 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowProcessDefModel extends ProcessDefinitionEntityImpl {
    private String xml;
    private String thumbnail;
}
