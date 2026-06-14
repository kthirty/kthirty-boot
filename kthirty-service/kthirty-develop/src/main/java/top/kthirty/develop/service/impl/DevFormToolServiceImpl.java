package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DbTypeUtil;
import com.mybatisflex.core.query.QueryCondition;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kthirty.core.db.base.entity.IdEntity;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormIndex;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.entity.table.DevFormIndexTableDef;
import top.kthirty.develop.entity.table.DevFormItemTableDef;
import top.kthirty.develop.enums.FieldAttribute;
import top.kthirty.develop.model.DevFormCodegenOption;
import top.kthirty.develop.model.DevFormSyncResultVO;
import top.kthirty.develop.service.DevFormIndexService;
import top.kthirty.develop.service.DevFormItemService;
import top.kthirty.develop.service.DevFormService;
import top.kthirty.develop.service.DevFormToolService;
import top.kthirty.develop.util.DevFormDdlBuilder;
import top.kthirty.develop.util.DevHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 表单开发工具服务
 */
@Service
@AllArgsConstructor
public class DevFormToolServiceImpl implements DevFormToolService {

    private final DevFormService devFormService;
    private final DevFormItemService devFormItemService;
    private final DevFormIndexService devFormIndexService;
    private final DevFormCodegenService devFormCodegenService;

    @Override
    public List<String> listDbTables() {
        return MetaUtil.getTables(getDataSource()).stream().sorted().toList();
    }

    @Override
    public DevForm previewImportTable(String tableName) {
        Assert.notBlank(tableName, "表名不能为空");
        return DevHelper.importTable(tableName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DevForm importTableFields(String formId, String tableName, boolean overwrite) {
        DevForm imported = previewImportTable(tableName);
        DevForm form = devFormService.getById(formId);
        Assert.notNull(form, "表单不存在");
        List<DevFormItem> currentItems = devFormItemService.list(
                DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(formId));
        Map<String, DevFormItem> currentMap = currentItems.stream()
                .filter(it -> StrUtil.isNotBlank(it.getColumnName()))
                .collect(Collectors.toMap(DevFormItem::getColumnName, it -> it, (a, b) -> a, LinkedHashMap::new));

        List<DevFormItem> merged = new ArrayList<>();
        double weight = 1;
        for (DevFormItem item : currentItems) {
            item.setWeight(weight++);
            merged.add(item);
        }

        for (DevFormItem item : imported.getItems()) {
            DevFormItem exists = currentMap.get(item.getColumnName());
            if (exists == null) {
                item.setId(null);
                item.setFormId(formId);
                item.setWeight(weight++);
                item.setIsDbSync(true);
                merged.add(item);
            } else if (overwrite) {
                item.setId(exists.getId());
                item.setFormId(formId);
                item.setWeight(exists.getWeight());
                merged.removeIf(it -> Objects.equals(it.getColumnName(), exists.getColumnName()));
                merged.add(item);
            }
        }

        List<DevFormIndex> indexes = devFormIndexService.list(
                DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(formId));
        if (CollUtil.isNotEmpty(imported.getIndexes())) {
            Set<String> indexNames = indexes.stream().map(DevFormIndex::getIndexName).collect(Collectors.toSet());
            for (DevFormIndex index : imported.getIndexes()) {
                if (!indexNames.contains(index.getIndexName())) {
                    index.setId(null);
                    index.setFormId(formId);
                    index.setIsDbSync(true);
                    indexes.add(index);
                }
            }
        }

        if (StrUtil.isBlank(form.getRemarks()) && StrUtil.isNotBlank(imported.getRemarks())) {
            form.setRemarks(imported.getRemarks());
        }
        form.setTableName(StrUtil.blankToDefault(form.getTableName(), imported.getTableName()));
        form.setIsDbSync(Constant.YES);
        form.setItems(merged);
        form.setIndexes(indexes);
        devFormService.updateById(form);

        DevForm result = devFormService.getById(formId);
        result.setItems(devFormItemService.list(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(formId)));
        result.setIndexes(devFormIndexService.list(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(formId)));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DevFormSyncResultVO syncToDb(String formId) {
        DevForm form = devFormService.getById(formId);
        Assert.notNull(form, "表单不存在");
        Assert.notBlank(form.getTableName(), "表名未配置");
        List<DevFormItem> items = ensureStandardColumns(devFormItemService.list(
                DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(formId)));
        Assert.notEmpty(items, "字段配置不能为空");

        DataSource dataSource = getDataSource();
        DbType dbType = DbTypeUtil.getDbType(dataSource);
        Table tableMeta = MetaUtil.getTableMeta(dataSource, form.getTableName());
        List<String> messages = new ArrayList<>();
        List<String> addedColumns = new ArrayList<>();
        List<String> addedIndexes = new ArrayList<>();
        boolean created = tableMeta == null;

        if (created) {
            executeDdl(buildCreateTableSql(dbType, form, items));
            messages.add("创建表 " + form.getTableName());
            for (DevFormItem item : items) {
                if (!FieldAttribute.NOT_DB_FIELD.getValue().equals(item.getFieldAttribute())) {
                    item.setIsDbSync(true);
                    saveOrUpdateItem(item, formId);
                }
            }
        } else {
            Set<String> existsColumns = tableMeta.getColumns().stream()
                    .map(Column::getName)
                    .collect(Collectors.toSet());
            for (DevFormItem item : items) {
                if (FieldAttribute.NOT_DB_FIELD.getValue().equals(item.getFieldAttribute())) {
                    continue;
                }
                if (!existsColumns.contains(item.getColumnName())) {
                    executeDdl(DevFormDdlBuilder.buildAddColumnSql(dbType, form.getTableName(), item));
                    addedColumns.add(item.getColumnName());
                    messages.add("新增字段 " + item.getColumnName());
                    item.setIsDbSync(true);
                    saveOrUpdateItem(item, formId);
                }
            }
        }

        Table refreshedMeta = MetaUtil.getTableMeta(dataSource, form.getTableName());
        List<DevFormIndex> indexes = devFormIndexService.list(
                DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID.eq(formId));
        if (CollUtil.isNotEmpty(indexes) && refreshedMeta != null) {
            Set<String> existsIndexes = refreshedMeta.getIndexInfoList().stream()
                    .map(it -> it.getIndexName().toLowerCase())
                    .collect(Collectors.toSet());
            for (DevFormIndex index : indexes) {
                if (existsIndexes.contains(index.getIndexName().toLowerCase())) {
                    continue;
                }
                try {
                    executeDdl(DevFormDdlBuilder.buildCreateIndexSql(dbType, form.getTableName(),
                            index.getIndexName(), index.getIndexType(), index.getColumnNames()));
                    addedIndexes.add(index.getIndexName());
                    messages.add("新增索引 " + index.getIndexName());
                    index.setIsDbSync(true);
                    devFormIndexService.updateById(index);
                } catch (Exception ex) {
                    messages.add("索引 " + index.getIndexName() + " 创建失败: " + ex.getMessage());
                }
            }
        }

        form.setIsDbSync(Constant.YES);
        devFormService.updateById(form);

        return DevFormSyncResultVO.builder()
                .success(true)
                .tableName(form.getTableName())
                .created(created)
                .addedColumns(addedColumns)
                .addedIndexes(addedIndexes)
                .messages(messages)
                .build();
    }

    @Override
    public void generateCode(String formId, DevFormCodegenOption option) {
        DevForm form = devFormService.getById(formId);
        Assert.notNull(form, "表单不存在");
        form.setItems(devFormItemService.list(QueryCondition.create(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID, formId)));
        form.setIndexes(devFormIndexService.list(QueryCondition.create(DevFormIndexTableDef.DEV_FORM_INDEX.FORM_ID, formId)));
        devFormCodegenService.generateAndDownload(form, option);
    }

    private void saveOrUpdateItem(DevFormItem item, String formId) {
        item.setFormId(formId);
        if (item.getId() == null) {
            devFormItemService.save(item);
        } else {
            devFormItemService.updateById(item);
        }
    }

    private String buildCreateTableSql(DbType dbType, DevForm form, List<DevFormItem> items) {
        List<String> lines = new ArrayList<>();
        String pkColumn = "id";
        for (DevFormItem item : items) {
            if (FieldAttribute.NOT_DB_FIELD.getValue().equals(item.getFieldAttribute())) {
                continue;
            }
            lines.add(DevFormDdlBuilder.buildColumnDefinition(dbType, item));
            if (FieldAttribute.PRIMARY_KEY.getValue().equals(item.getFieldAttribute())) {
                pkColumn = item.getColumnName();
            }
        }
        String quote = dbType == DbType.MYSQL ? "`" : "\"";
        lines.add("PRIMARY KEY (" + quote + pkColumn + quote + ")");
        String comment = StrUtil.blankToDefault(form.getRemarks(), form.getTableName()).replace("'", "''");
        return "CREATE TABLE " + quote + form.getTableName() + quote
                + " (\n  " + String.join(",\n  ", lines) + "\n) COMMENT='" + comment + "'";
    }

    @SneakyThrows
    private void executeDdl(String sql) {
        try (Connection connection = getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    private DataSource getDataSource() {
        DataSource dataSource = SpringUtil.getBeanSafe(DataSource.class);
        Assert.notNull(dataSource, "dataSource is null");
        return dataSource;
    }

    private List<DevFormItem> ensureStandardColumns(List<DevFormItem> items) {
        List<DevFormItem> result = CollUtil.isEmpty(items) ? new ArrayList<>() : new ArrayList<>(items);
        Set<String> names = result.stream().map(DevFormItem::getColumnName).filter(StrUtil::isNotBlank).collect(Collectors.toSet());
        if (!names.contains("id")) {
            result.add(0, standardItem("id", "varchar", 32, null, "主键", "java.lang.String", FieldAttribute.PRIMARY_KEY, false, 1D));
        }
        double weight = result.stream().map(DevFormItem::getWeight).filter(Objects::nonNull).mapToDouble(Double::doubleValue).max().orElse(1D);
        if (!names.contains("create_by")) {
            result.add(standardItem("create_by", "varchar", 32, null, "创建人", "java.lang.String", FieldAttribute.NORMAL, true, ++weight));
        }
        if (!names.contains("update_by")) {
            result.add(standardItem("update_by", "varchar", 32, null, "更新人", "java.lang.String", FieldAttribute.NORMAL, true, ++weight));
        }
        if (!names.contains("create_date")) {
            result.add(standardItem("create_date", "timestamp", null, null, "创建时间", "java.util.Date", FieldAttribute.NORMAL, true, ++weight));
        }
        if (!names.contains("update_date")) {
            result.add(standardItem("update_date", "timestamp", null, null, "更新时间", "java.util.Date", FieldAttribute.NORMAL, true, ++weight));
        }
        if (!names.contains("deleted")) {
            result.add(standardItem("deleted", "tinyint", 1, null, "删除标记", "java.lang.Boolean", FieldAttribute.DEL_FLAG, true, ++weight));
        }
        return result;
    }

    private DevFormItem standardItem(String columnName, String columnType, Integer length, Integer point,
                                     String remarks, String fieldType, FieldAttribute attribute,
                                     boolean nullable, double weight) {
        return DevFormItem.builder()
                .columnName(columnName)
                .columnType(columnType)
                .columnLength(length)
                .columnPointLength(point)
                .columnRemarks(remarks)
                .fieldType(fieldType)
                .fieldAttribute(attribute.getValue())
                .columnNullable(nullable)
                .formRequired(!nullable && attribute.isNormal())
                .isShowForm(attribute.isNormal())
                .isShowList(attribute.isNormal())
                .isShowQuery(false)
                .isAllowSort(false)
                .isReadonly(false)
                .weight(weight)
                .build();
    }
}
