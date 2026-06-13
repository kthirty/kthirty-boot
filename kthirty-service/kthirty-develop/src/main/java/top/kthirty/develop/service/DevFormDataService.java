package top.kthirty.develop.service;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.db.support.Query;
import top.kthirty.develop.model.DevFormImportResultVO;
import top.kthirty.develop.model.DevFormSchemaVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 动态表单数据服务
 */
public interface DevFormDataService {

    DevFormSchemaVO getSchema(String formId);

    Page<Map<String, Object>> page(String formId, Query<?> query, Map<String, String> params);

    List<Map<String, Object>> list(String formId, Map<String, String> params);

    List<Map<String, Object>> tree(String formId, Map<String, String> params);

    Map<String, Object> getInfo(String formId, Serializable id);

    boolean save(String formId, Map<String, Object> data);

    boolean update(String formId, Map<String, Object> data);

    boolean remove(String formId, Serializable id);

    boolean removeBatch(String formId, List<? extends Serializable> ids);

    void export(String formId, Map<String, String> params);

    void importTemplate(String formId);

    DevFormImportResultVO importData(String formId, MultipartFile file);
}
