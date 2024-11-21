package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.impl.persistence.entity.ModelEntityImpl;
/**
 * @description 模型文件
 * @author KThirty
 * @since 2024/11/21 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowModel extends ModelEntityImpl {
    private String xml;
}
