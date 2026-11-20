package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.develop.config.DevReportDataSourceManager;
import top.kthirty.develop.entity.DevReport;
import top.kthirty.develop.entity.DevReportItem;
import top.kthirty.develop.entity.table.DevReportItemTableDef;
import top.kthirty.develop.mapper.DevReportMapper;
import top.kthirty.develop.service.DevReportItemService;
import top.kthirty.develop.service.DevReportService;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 报表开发 服务层实现。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Service
@AllArgsConstructor
public class DevReportServiceImpl extends ServiceImpl<DevReportMapper, DevReport> implements DevReportService {

    private final DevReportItemService devReportItemService;
    private final DevReportDataSourceManager dataSourceManager;

    @Override
    public boolean save(DevReport entity) {
        normalize(entity);
        Assert.isTrue(super.save(entity), "保存失败");
        saveItems(entity);
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
        devReportItemService.remove(DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID.eq(id));
        return super.removeById(id);
    }

    @Override
    public boolean updateById(DevReport entity) {
        normalize(entity);
        List<DevReportItem> items = CollUtil.emptyIfNull(entity.getItems());

        List<String> keepItemIds = items.stream().map(IdEntity::getId).filter(Objects::nonNull).toList();
        if (CollUtil.isNotEmpty(keepItemIds)) {
            devReportItemService.remove(DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID.eq(entity.getId())
                    .and(DevReportItemTableDef.DEV_REPORT_ITEM.ID.notIn(keepItemIds)));
        } else {
            devReportItemService.remove(DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID.eq(entity.getId()));
        }

        Map<String, DevReportItem> itemMap = devReportItemService.list(
                        DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID.eq(entity.getId()))
                .stream().collect(Collectors.toMap(IdEntity::getId, Function.identity(), (a, b) -> a));

        for (DevReportItem it : items) {
            it.setReportId(entity.getId());
            if (it.getId() == null || !itemMap.containsKey(it.getId())) {
                it.setId(null);
                devReportItemService.save(it);
            } else {
                devReportItemService.updateById(it);
            }
        }

        Assert.isTrue(super.updateById(entity), "保存失败");
        return true;
    }

    @Override
    @SneakyThrows
    public List<DevReportItem> parseSqlFields(String reportSql, String dsKey) {
        Assert.notBlank(reportSql, "SQL不能为空");
        String sql = stripTrailingSemicolon(reportSql.trim());
        Assert.isTrue(StrUtil.startWithIgnoreCase(sql, "select")
                        || StrUtil.startWithIgnoreCase(sql, "with"),
                "仅支持 SELECT / WITH 查询语句");

        DataSource dataSource = dataSourceManager.getDataSource(dsKey);
        String metaSql = "SELECT * FROM (" + sql + ") _dev_report_meta WHERE 1 = 0";

        List<DevReportItem> items = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(metaSql);
             ResultSet rs = statement.executeQuery()) {
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String label = StrUtil.blankToDefault(meta.getColumnLabel(i), meta.getColumnName(i));
                String fieldType = resolveFieldType(meta.getColumnType(i), meta.getColumnClassName(i));
                items.add(DevReportItem.builder()
                        .columnName(label)
                        .columnRemarks(label)
                        .fieldType(fieldType)
                        .dictCode("Boolean".equals(fieldType) ? "whether" : null)
                        .weight((double) i)
                        .isShowQuery(Constant.NO)
                        .queryComponent(inferQueryComponent(fieldType))
                        .isShowList(Constant.YES)
                        .isAllowSort(Constant.NO)
                        .listComponent(inferListComponent(fieldType))
                        .build());
            }
        }
        return items;
    }

    private void saveItems(DevReport entity) {
        if (CollUtil.isNotEmpty(entity.getItems())) {
            entity.getItems().forEach(it ->
                    it.setReportId(entity.getId())
                            .setId(null));
            devReportItemService.saveBatch(entity.getItems());
        }
    }

    private void normalize(DevReport entity) {
        Assert.notBlank(entity.getCode(), "报表编码不能为空");
        Assert.notBlank(entity.getName(), "报表名称不能为空");
        entity.setIsPage(toWhether(entity.getIsPage(), Constant.YES));
        normalizeItems(entity);
        if (StrUtil.isBlank(entity.getDsKey())) {
            entity.setDsKey(DevReportDataSourceManager.PRIMARY_KEY);
        }
        QueryWrapper codeQuery = QueryWrapper.create().eq(DevReport::getCode, entity.getCode());
        if (StrUtil.isNotBlank(entity.getId())) {
            codeQuery.ne(DevReport::getId, entity.getId());
        }
        Assert.isFalse(exists(codeQuery), "报表编码已存在");
    }

    private void normalizeItems(DevReport entity) {
        if (CollUtil.isEmpty(entity.getItems())) {
            return;
        }
        for (DevReportItem item : entity.getItems()) {
            item.setIsShowQuery(toWhether(item.getIsShowQuery(), Constant.NO));
            item.setIsShowList(toWhether(item.getIsShowList(), Constant.YES));
            item.setIsAllowSort(toWhether(item.getIsAllowSort(), Constant.NO));
            if ("Boolean".equalsIgnoreCase(item.getFieldType()) && StrUtil.isBlank(item.getDictCode())) {
                item.setDictCode("whether");
            }
        }
    }

    private static String toWhether(String value, String defaultValue) {
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        if (Constant.YES.equals(value) || "true".equalsIgnoreCase(value)) {
            return Constant.YES;
        }
        if (Constant.NO.equals(value) || "false".equalsIgnoreCase(value)) {
            return Constant.NO;
        }
        return defaultValue;
    }

    private static String stripTrailingSemicolon(String sql) {
        String result = sql;
        while (result.endsWith(";")) {
            result = result.substring(0, result.length() - 1).trim();
        }
        return result;
    }

    private static String resolveFieldType(int jdbcType, String className) {
        return switch (jdbcType) {
            case Types.BIT, Types.BOOLEAN -> "Boolean";
            case Types.TINYINT, Types.SMALLINT, Types.INTEGER -> "Integer";
            case Types.BIGINT -> "Long";
            case Types.FLOAT, Types.REAL, Types.DOUBLE -> "Double";
            case Types.NUMERIC, Types.DECIMAL -> "BigDecimal";
            case Types.DATE -> "Date";
            case Types.TIME -> "Time";
            case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE -> "DateTime";
            default -> {
                if (StrUtil.isNotBlank(className)) {
                    int idx = className.lastIndexOf('.');
                    yield idx >= 0 ? className.substring(idx + 1) : className;
                }
                yield "String";
            }
        };
    }

    private static String inferQueryComponent(String fieldType) {
        if (StrUtil.containsAnyIgnoreCase(fieldType, "Date", "Time")) {
            return fieldType.contains("Time") && !fieldType.equals("Time") ? "DateTimePicker" : "DatePicker";
        }
        if (StrUtil.containsAnyIgnoreCase(fieldType, "Integer", "Long", "Double", "BigDecimal", "Float")) {
            return "InputNumber";
        }
        if (StrUtil.containsIgnoreCase(fieldType, "Boolean")) {
            return "Select";
        }
        return "Input";
    }

    private static String inferListComponent(String fieldType) {
        if (StrUtil.containsIgnoreCase(fieldType, "Boolean")) {
            return "Tag";
        }
        return "Text";
    }
}
