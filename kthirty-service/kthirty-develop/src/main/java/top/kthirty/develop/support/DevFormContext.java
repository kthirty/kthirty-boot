package top.kthirty.develop.support;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.enums.FieldAttribute;
import top.kthirty.develop.enums.ListType;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DevForm 运行时上下文
 */
@Getter
public class DevFormContext {

    private final DevForm form;
    private final List<DevFormItem> items;
    private final DevFormItem primaryKeyItem;
    private final DevFormItem logicDeleteItem;
    private final Map<String, DevFormItem> columnMap;
    private final Set<String> dbColumns;

    public DevFormContext(DevForm form, List<DevFormItem> items) {
        Assert.notNull(form, "表单不存在");
        Assert.notBlank(form.getTableName(), "表名未配置");
        Assert.notEmpty(items, "表单字段未配置");
        this.form = form;
        this.items = items.stream()
                .sorted(Comparator.comparingDouble(it -> it.getWeight() == null ? 0D : it.getWeight()))
                .toList();
        this.columnMap = this.items.stream()
                .filter(it -> StrUtil.isNotBlank(it.getColumnName()))
                .collect(Collectors.toMap(DevFormItem::getColumnName, it -> it, (a, b) -> a, LinkedHashMap::new));
        this.dbColumns = this.items.stream()
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .map(DevFormItem::getColumnName)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        this.primaryKeyItem = this.items.stream()
                .filter(it -> FieldAttribute.PRIMARY_KEY.getValue().equals(it.getFieldAttribute()))
                .findFirst()
                .orElse(null);
        this.logicDeleteItem = this.items.stream()
                .filter(it -> FieldAttribute.DEL_FLAG.getValue().equals(it.getFieldAttribute()))
                .findFirst()
                .orElse(null);
    }

    public String getTableName() {
        return form.getTableName();
    }

    public String getPrimaryKeyColumn() {
        return primaryKeyItem == null ? "id" : primaryKeyItem.getColumnName();
    }

    public String getLogicDeleteColumn() {
        return logicDeleteItem == null ? "deleted" : logicDeleteItem.getColumnName();
    }

    public boolean hasLogicDelete() {
        return columnMap.containsKey(getLogicDeleteColumn());
    }

    public boolean isTreeList() {
        return ListType.TREE.getValue().equals(form.getListType());
    }

    public DevFormItem getItem(String columnName) {
        return columnMap.get(columnName);
    }

    public boolean isDbColumn(String columnName) {
        return dbColumns.contains(columnName);
    }

    public static String toFieldName(String columnName) {
        return StrUtil.toCamelCase(columnName);
    }

    public static String toColumnName(String fieldName) {
        return StrUtil.toUnderlineCase(fieldName);
    }

    public String resolveColumnName(String fieldOrColumn) {
        if (StrUtil.isBlank(fieldOrColumn)) {
            return fieldOrColumn;
        }
        if (columnMap.containsKey(fieldOrColumn)) {
            return fieldOrColumn;
        }
        String columnName = toColumnName(fieldOrColumn);
        Assert.isTrue(columnMap.containsKey(columnName), "非法字段: {}", fieldOrColumn);
        return columnName;
    }

    public List<DevFormItem> listColumns() {
        return items.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowList()))
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .toList();
    }

    public List<DevFormItem> queryColumns() {
        return items.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowQuery()))
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .toList();
    }

    public List<DevFormItem> formColumns() {
        return items.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowForm()))
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .filter(it -> !FieldAttribute.DEL_FLAG.getValue().equals(it.getFieldAttribute()))
                .toList();
    }

    public List<DevFormItem> importColumns() {
        return formColumns().stream()
                .filter(it -> FieldAttribute.NORMAL.getValue().equals(it.getFieldAttribute()))
                .toList();
    }

    public boolean isPrimaryKey(String columnName) {
        return primaryKeyItem != null && Objects.equals(primaryKeyItem.getColumnName(), columnName);
    }
}
