package top.kthirty.develop.support;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.develop.entity.DevReport;
import top.kthirty.develop.entity.DevReportItem;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DevReport 运行时上下文
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Getter
public class DevReportContext {

    private final DevReport report;
    private final List<DevReportItem> items;
    private final Map<String, DevReportItem> columnMap;
    private final Map<String, DevReportItem> fieldMap;

    public DevReportContext(DevReport report, List<DevReportItem> items) {
        Assert.notNull(report, "报表不存在");
        Assert.notBlank(report.getReportSql(), "报表SQL未配置");
        this.report = report;
        this.items = items == null ? List.of() : items.stream()
                .sorted(Comparator.comparingDouble(it -> it.getWeight() == null ? 0D : it.getWeight()))
                .toList();
        this.columnMap = this.items.stream()
                .filter(it -> StrUtil.isNotBlank(it.getColumnName()))
                .collect(Collectors.toMap(DevReportItem::getColumnName, it -> it, (a, b) -> a, LinkedHashMap::new));
        this.fieldMap = this.items.stream()
                .filter(it -> StrUtil.isNotBlank(it.getColumnName()))
                .collect(Collectors.toMap(it -> toFieldName(it.getColumnName()), it -> it, (a, b) -> a, LinkedHashMap::new));
    }

    public boolean isPage() {
        return isYes(report.getIsPage(), true);
    }

    public static boolean isYes(String value, boolean defaultValue) {
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return Constant.YES.equals(value) || "true".equalsIgnoreCase(value);
    }

    public DevReportItem getItemByColumn(String columnName) {
        return columnMap.get(columnName);
    }

    public DevReportItem resolveItem(String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        DevReportItem item = columnMap.get(key);
        if (item != null) {
            return item;
        }
        item = fieldMap.get(key);
        if (item != null) {
            return item;
        }
        // 忽略大小写匹配列名
        for (Map.Entry<String, DevReportItem> entry : columnMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static String toFieldName(String columnName) {
        if (StrUtil.isBlank(columnName)) {
            return columnName;
        }
        String name = columnName.contains(".")
                ? columnName.substring(columnName.lastIndexOf('.') + 1)
                : columnName;
        return StrUtil.toCamelCase(name.toLowerCase(Locale.ROOT));
    }
}
