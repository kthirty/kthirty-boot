package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.impl.persistence.entity.ModelEntityImpl;
import org.flowable.engine.repository.Model;
import top.kthirty.core.tool.utils.BeanUtil;

/**
 * @description 模型文件
 * @author KThirty
 * @since 2024/11/21 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowModel extends ModelEntityImpl {
    private String xml;
    private String thumbnail;

    public Model getModel(){
        Model modelEntity = new ModelEntityImpl();
        BeanUtil.copyProperties(this,modelEntity);
        return modelEntity;
    }
    public FlowModel setByModel(Model model){
        BeanUtil.copyProperties(model,this);
        return this;
    }
}
