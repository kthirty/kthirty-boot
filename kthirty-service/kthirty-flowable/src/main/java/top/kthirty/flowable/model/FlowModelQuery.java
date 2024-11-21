package top.kthirty.flowable.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.engine.repository.Model;
import top.kthirty.core.db.support.Query;
/**
 * @description 模型文件查询
 * @author KThirty
 * @since 2024/11/21 15:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FlowModelQuery extends Query<Model> {
    private String key;
    private String name;
    private Boolean deployed;
}
