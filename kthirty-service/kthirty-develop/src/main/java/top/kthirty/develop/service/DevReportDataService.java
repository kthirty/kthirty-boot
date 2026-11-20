package top.kthirty.develop.service;

import com.mybatisflex.core.paginate.Page;
import top.kthirty.core.db.support.Query;
import top.kthirty.develop.model.DevReportSchemaVO;

import java.util.List;
import java.util.Map;

/**
 * 报表动态数据服务
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
public interface DevReportDataService {

    DevReportSchemaVO getSchema(String reportId);

    DevReportSchemaVO getSchemaByCode(String code);

    Page<Map<String, Object>> page(String reportId, Query<?> query, Map<String, String> params);

    List<Map<String, Object>> list(String reportId, Map<String, String> params);

    Page<Map<String, Object>> pageByCode(String code, Query<?> query, Map<String, String> params);

    List<Map<String, Object>> listByCode(String code, Map<String, String> params);

    void export(String reportId, Map<String, String> params);

    void exportByCode(String code, Map<String, String> params);
}
