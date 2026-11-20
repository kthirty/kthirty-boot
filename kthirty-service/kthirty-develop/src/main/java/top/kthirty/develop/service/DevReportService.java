package top.kthirty.develop.service;

import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.develop.entity.DevReport;
import top.kthirty.develop.entity.DevReportItem;

import java.util.List;

/**
 * 报表开发 服务层。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
public interface DevReportService extends BaseService<DevReport> {

    /**
     * 解析 SQL 字段（基于 ResultSetMetaData）
     *
     * @param reportSql 报表SQL
     * @param dsKey     数据源Key，空则使用主数据源
     * @return 解析出的字段列表
     */
    List<DevReportItem> parseSqlFields(String reportSql, String dsKey);

}
