package top.kthirty.flowable.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.repository.Model;

/**
 * @description 模型文件
 * @author KThirty
 * @since 2024/11/21 15:59
 */
@Data
@NoArgsConstructor
public class FlowModel {
    private String id;
    private String name;
    private String key;
    private String category;
    private String metaInfo;
    private String tenantId = ProcessEngineConfiguration.NO_TENANT_ID;
    private String xml;
    private String thumbnail;

    public FlowModel(Model model){
        this.id = model.getId();
        this.name = model.getName();
        this.key = model.getKey();
        this.category = model.getCategory();
        this.metaInfo = model.getMetaInfo();
        this.tenantId = model.getTenantId();
    }

}
