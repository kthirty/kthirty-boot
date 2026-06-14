package top.kthirty.develop.service;

import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.model.DevFormCodegenOption;
import top.kthirty.develop.model.DevFormSyncResultVO;

import java.util.List;

/**
 * 表单开发扩展服务
 */
public interface DevFormToolService {

    List<String> listDbTables();

    DevForm previewImportTable(String tableName);

    DevForm importTableFields(String formId, String tableName, boolean overwrite);

    DevFormSyncResultVO syncToDb(String formId);

    void generateCode(String formId, DevFormCodegenOption option);
}
