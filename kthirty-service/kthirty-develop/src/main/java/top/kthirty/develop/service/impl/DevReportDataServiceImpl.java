package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.tool.dict.DictUtil;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.web.utils.WebUtil;
import top.kthirty.develop.config.DevReportDataSourceManager;
import top.kthirty.develop.entity.DevReport;
import top.kthirty.develop.entity.DevReportItem;
import top.kthirty.develop.entity.table.DevReportItemTableDef;
import top.kthirty.develop.model.DevReportColumnSchemaVO;
import top.kthirty.develop.model.DevReportSchemaVO;
import top.kthirty.develop.service.DevReportDataService;
import top.kthirty.develop.service.DevReportItemService;
import top.kthirty.develop.service.DevReportService;
import top.kthirty.develop.support.DevReportContext;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 报表动态数据服务实现
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Service
@AllArgsConstructor
public class DevReportDataServiceImpl implements DevReportDataService {

    private static final Set<String> IGNORE_PARAMS = Set.of(
            "reportId", "code", "pageNumber", "pageSize", "sortField", "sortOrder", "current", "size"
    );

    private static final Pattern SAFE_IDENT = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");

    private final DevReportService devReportService;
    private final DevReportItemService devReportItemService;
    private final DevReportDataSourceManager dataSourceManager;

    @Override
    public DevReportSchemaVO getSchema(String reportId) {
        return buildSchema(loadContextById(reportId));
    }

    @Override
    public DevReportSchemaVO getSchemaByCode(String code) {
        return buildSchema(loadContextByCode(code));
    }

    @Override
    public Page<Map<String, Object>> page(String reportId, Query<?> query, Map<String, String> params) {
        return doPage(loadContextById(reportId), query, params);
    }

    @Override
    public List<Map<String, Object>> list(String reportId, Map<String, String> params) {
        return doList(loadContextById(reportId), params);
    }

    @Override
    public Page<Map<String, Object>> pageByCode(String code, Query<?> query, Map<String, String> params) {
        return doPage(loadContextByCode(code), query, params);
    }

    @Override
    public List<Map<String, Object>> listByCode(String code, Map<String, String> params) {
        return doList(loadContextByCode(code), params);
    }

    @Override
    public void export(String reportId, Map<String, String> params) {
        doExport(loadContextById(reportId), params);
    }

    @Override
    public void exportByCode(String code, Map<String, String> params) {
        doExport(loadContextByCode(code), params);
    }

    private DevReportSchemaVO buildSchema(DevReportContext ctx) {
        DevReport report = ctx.getReport();
        return DevReportSchemaVO.builder()
                .reportId(report.getId())
                .code(report.getCode())
                .name(report.getName())
                .isPage(ctx.isPage() ? Constant.YES : Constant.NO)
                .dsKey(report.getDsKey())
                .remarks(report.getRemarks())
                .columns(ctx.getItems().stream()
                        .map(it -> DevReportColumnSchemaVO.builder()
                                .columnName(it.getColumnName())
                                .fieldName(DevReportContext.toFieldName(it.getColumnName()))
                                .label(StrUtil.blankToDefault(it.getColumnRemarks(), it.getColumnName()))
                                .fieldType(it.getFieldType())
                                .showInList(DevReportContext.isYes(it.getIsShowList(), true))
                                .allowSort(DevReportContext.isYes(it.getIsAllowSort(), false))
                                .showInQuery(DevReportContext.isYes(it.getIsShowQuery(), false))
                                .queryComponent(StrUtil.blankToDefault(it.getQueryComponent(), "Input"))
                                .listComponent(StrUtil.blankToDefault(it.getListComponent(), "Text"))
                                .dictCode(StrUtil.blankToDefault(it.getDictCode(),
                                        "Boolean".equalsIgnoreCase(it.getFieldType()) ? "whether" : null))
                                .weight(it.getWeight())
                                .build())
                        .toList())
                .build();
    }

    @SneakyThrows
    private Page<Map<String, Object>> doPage(DevReportContext ctx, Query<?> query, Map<String, String> params) {
        Assert.isTrue(ctx.isPage(), "当前报表未开启分页");
        query.defaultPage();
        SqlParts parts = buildSql(ctx, params);
        DataSource dataSource = dataSourceManager.getDataSource(ctx.getReport().getDsKey());

        long total;
        String countSql = "SELECT COUNT(1) FROM (" + parts.filteredSql() + ") _dev_report_cnt";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement countPs = connection.prepareStatement(countSql)) {
            bindParams(countPs, parts.params());
            try (ResultSet rs = countPs.executeQuery()) {
                rs.next();
                total = rs.getLong(1);
            }
        }

        String pageSql = parts.filteredSql() + parts.orderBy()
                + " LIMIT " + query.getPageSize() + " OFFSET " + query.getFirstResult();
        List<Map<String, Object>> records;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(pageSql)) {
            bindParams(ps, parts.params());
            try (ResultSet rs = ps.executeQuery()) {
                records = convertRows(ctx, rs);
            }
        }

        Page<Map<String, Object>> page = new Page<>();
        page.setPageNumber(query.getPageNumber());
        page.setPageSize(query.getPageSize());
        page.setTotalRow(total);
        page.setRecords(records);
        return page;
    }

    @SneakyThrows
    private List<Map<String, Object>> doList(DevReportContext ctx, Map<String, String> params) {
        SqlParts parts = buildSql(ctx, params);
        DataSource dataSource = dataSourceManager.getDataSource(ctx.getReport().getDsKey());
        String sql = parts.filteredSql() + parts.orderBy();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            bindParams(ps, parts.params());
            try (ResultSet rs = ps.executeQuery()) {
                return convertRows(ctx, rs);
            }
        }
    }

    private SqlParts buildSql(DevReportContext ctx, Map<String, String> params) {
        String baseSql = stripTrailingSemicolon(ctx.getReport().getReportSql().trim());
        Assert.isTrue(StrUtil.startWithIgnoreCase(baseSql, "select")
                        || StrUtil.startWithIgnoreCase(baseSql, "with"),
                "仅支持 SELECT / WITH 查询语句");

        StringBuilder where = new StringBuilder();
        List<Object> bindParams = new ArrayList<>();
        if (CollUtil.isNotEmpty(params)) {
            params.forEach((key, value) -> {
                if (IGNORE_PARAMS.contains(key) || StrUtil.isBlank(value)) {
                    return;
                }
                DevReportItem item = ctx.resolveItem(key);
                if (item == null || !DevReportContext.isYes(item.getIsShowQuery(), false)) {
                    return;
                }
                String column = quoteIdent(item.getColumnName());
                String normalized = normalizeQueryValue(value);
                if (StrUtil.startWith(value, "*") || StrUtil.endWith(value, "*")) {
                    where.append(" AND ").append(column).append(" LIKE ?");
                    bindParams.add("%" + normalized + "%");
                } else {
                    where.append(" AND ").append(column).append(" = ?");
                    bindParams.add(normalized);
                }
            });
        }

        String filtered = "SELECT * FROM (" + baseSql + ") _dev_report_data WHERE 1 = 1" + where;
        String orderBy = buildOrderBy(ctx, params);
        return new SqlParts(filtered, orderBy, bindParams);
    }

    private String buildOrderBy(DevReportContext ctx, Map<String, String> params) {
        if (params == null) {
            return "";
        }
        String sortField = params.get("sortField");
        String sortOrder = params.get("sortOrder");
        if (StrUtil.isBlank(sortField)) {
            return "";
        }
        DevReportItem item = ctx.resolveItem(sortField);
        if (item == null || !DevReportContext.isYes(item.getIsAllowSort(), false)) {
            return "";
        }
        boolean asc = !"desc".equalsIgnoreCase(sortOrder);
        return " ORDER BY " + quoteIdent(item.getColumnName()) + (asc ? " ASC" : " DESC");
    }

    private List<Map<String, Object>> convertRows(DevReportContext ctx, ResultSet rs) throws Exception {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        List<String> labels = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            labels.add(StrUtil.blankToDefault(meta.getColumnLabel(i), meta.getColumnName(i)));
        }

        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= count; i++) {
                String column = labels.get(i - 1);
                Object value = normalizeWhetherValue(rs.getObject(i));
                String fieldName = DevReportContext.toFieldName(column);
                DevReportItem item = ctx.getItemByColumn(column);
                if (item == null) {
                    item = ctx.resolveItem(column);
                }
                value = normalizeWhetherValue(item, value);
                String dictCode = resolveDictCode(item);
                if (StrUtil.isNotBlank(dictCode) && value != null) {
                    row.put(fieldName, value);
                    row.put(fieldName + "Label", DictUtil.getLabel(dictCode, String.valueOf(value)));
                } else {
                    row.put(fieldName, value);
                }
            }
            rows.add(row);
        }
        return rows;
    }

    private DevReportContext loadContextById(String reportId) {
        DevReport report = devReportService.getById(reportId);
        Assert.notNull(report, "报表不存在");
        return new DevReportContext(report, loadItems(report.getId()));
    }

    private DevReportContext loadContextByCode(String code) {
        Assert.notBlank(code, "报表编码不能为空");
        DevReport report = devReportService.getOne(QueryWrapper.create().eq(DevReport::getCode, code));
        Assert.notNull(report, "报表不存在");
        return new DevReportContext(report, loadItems(report.getId()));
    }

    private List<DevReportItem> loadItems(String reportId) {
        return devReportItemService.list(DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID.eq(reportId));
    }

    @SneakyThrows
    private void doExport(DevReportContext ctx, Map<String, String> params) {
        List<DevReportItem> columns = ctx.getItems().stream()
                .filter(it -> DevReportContext.isYes(it.getIsShowList(), true))
                .sorted(Comparator.comparingDouble(it -> it.getWeight() == null ? 0D : it.getWeight()))
                .toList();
        Assert.notEmpty(columns, "没有可导出的列");
        List<Map<String, Object>> rows = doList(ctx, params);

        File file = FileUtil.createTempFile(".xlsx", true);
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            writer.writeHeadRow(columns.stream()
                    .map(it -> StrUtil.blankToDefault(it.getColumnRemarks(), it.getColumnName()))
                    .toList());
            for (Map<String, Object> row : rows) {
                List<Object> line = columns.stream()
                        .map(it -> formatExportValue(it, row.get(DevReportContext.toFieldName(it.getColumnName()))))
                        .toList();
                writer.writeRow(line);
            }
            writer.flush(FileUtil.getOutputStream(file));
        }
        String baseName = StrUtil.blankToDefault(ctx.getReport().getName(), ctx.getReport().getCode());
        String fileName = baseName + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        WebUtil.renderFile(file, fileName);
    }

    private Object formatExportValue(DevReportItem item, Object value) {
        if (value == null) {
            return "";
        }
        value = normalizeWhetherValue(item, value);
        String dictCode = resolveDictCode(item);
        if (StrUtil.isNotBlank(dictCode)) {
            String label = DictUtil.getLabel(dictCode, Convert.toStr(value));
            return StrUtil.blankToDefault(label, Convert.toStr(value));
        }
        return value;
    }

    /**
     * 布尔 / 是否值统一成字典使用的 1/0 字符串。
     */
    private static Object normalizeWhetherValue(Object value) {
        if (value instanceof Boolean bool) {
            return bool ? Constant.YES : Constant.NO;
        }
        return value;
    }

    private static Object normalizeWhetherValue(DevReportItem item, Object value) {
        value = normalizeWhetherValue(value);
        if (value == null || item == null || !"Boolean".equalsIgnoreCase(item.getFieldType())) {
            return value;
        }
        String text = String.valueOf(value);
        if (Constant.YES.equals(text) || "true".equalsIgnoreCase(text)) {
            return Constant.YES;
        }
        if (Constant.NO.equals(text) || "false".equalsIgnoreCase(text)) {
            return Constant.NO;
        }
        return value;
    }

    private static String resolveDictCode(DevReportItem item) {
        if (item == null) {
            return null;
        }
        if (StrUtil.isNotBlank(item.getDictCode())) {
            return item.getDictCode();
        }
        if ("Boolean".equalsIgnoreCase(item.getFieldType())) {
            return "whether";
        }
        return null;
    }

    private static void bindParams(PreparedStatement ps, List<Object> params) throws Exception {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    private static String normalizeQueryValue(String value) {
        if (StrUtil.startWith(value, "*") && StrUtil.endWith(value, "*")) {
            return StrUtil.removeSuffix(StrUtil.removePrefix(value, "*"), "*");
        }
        if (StrUtil.startWith(value, "*")) {
            return StrUtil.removePrefix(value, "*");
        }
        if (StrUtil.endWith(value, "*")) {
            return StrUtil.removeSuffix(value, "*");
        }
        return value;
    }

    private static String quoteIdent(String name) {
        Assert.notBlank(name, "字段名不能为空");
        Assert.isTrue(SAFE_IDENT.matcher(name).matches(), "非法字段名: {}", name);
        return "`" + name + "`";
    }

    private static String stripTrailingSemicolon(String sql) {
        String result = sql;
        while (result.endsWith(";")) {
            result = result.substring(0, result.length() - 1).trim();
        }
        return result;
    }

    private record SqlParts(String filteredSql, String orderBy, List<Object> params) {
    }
}
