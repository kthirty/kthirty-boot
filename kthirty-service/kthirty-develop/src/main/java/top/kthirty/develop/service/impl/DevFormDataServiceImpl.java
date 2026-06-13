package top.kthirty.develop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.tool.dict.DictUtil;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.web.utils.WebUtil;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.entity.table.DevFormItemTableDef;
import top.kthirty.develop.enums.FieldAttribute;
import top.kthirty.develop.enums.ListType;
import top.kthirty.develop.model.*;
import top.kthirty.develop.service.DevFormDataService;
import top.kthirty.develop.service.DevFormItemService;
import top.kthirty.develop.service.DevFormService;
import top.kthirty.develop.support.DevFormContext;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态表单数据服务实现
 */
@Service
@AllArgsConstructor
public class DevFormDataServiceImpl implements DevFormDataService {

    private static final Set<String> IGNORE_PARAMS = Set.of(
            "formId", "pageNumber", "pageSize", "sortField", "sortOrder", "current", "size"
    );

    private static final List<String> AUDIT_COLUMNS = List.of(
            "create_by", "create_date", "update_by", "update_date"
    );

    private final DevFormService devFormService;
    private final DevFormItemService devFormItemService;

    @Override
    public DevFormSchemaVO getSchema(String formId) {
        DevFormContext ctx = loadContext(formId);
        DevForm form = ctx.getForm();
        String primaryKey = ctx.getPrimaryKeyColumn();
        return DevFormSchemaVO.builder()
                .formId(form.getId())
                .tableName(form.getTableName())
                .title(form.getRemarks())
                .tableType(form.getTableType())
                .listType(form.getListType())
                .isDbSync(form.getIsDbSync())
                .primaryKey(primaryKey)
                .primaryKeyField(DevFormContext.toFieldName(primaryKey))
                .logicDeleteField(ctx.hasLogicDelete() ? ctx.getLogicDeleteColumn() : null)
                .auditFields(AUDIT_COLUMNS.stream()
                        .filter(it -> ctx.getColumnMap().containsKey(it))
                        .map(DevFormContext::toFieldName)
                        .toList())
                .treeConfig(buildTreeConfig(form))
                .columns(buildColumnSchemas(ctx))
                .features(DevFormFeaturesVO.builder()
                        .enableAdd(true)
                        .enableEdit(true)
                        .enableDelete(true)
                        .enableExport(true)
                        .enableImport(true)
                        .enableBatchDelete(true)
                        .build())
                .build();
    }

    @Override
    public Page<Map<String, Object>> page(String formId, Query<?> query, Map<String, String> params) {
        DevFormContext ctx = loadContext(formId);
        QueryWrapper wrapper = buildQueryWrapper(ctx, params);
        applySort(ctx, wrapper, params);
        Page<Row> rowPage = Db.paginate(ctx.getTableName(),
                query.getPageNumber(), query.getPageSize(), wrapper);
        Page<Map<String, Object>> page = new Page<>();
        page.setPageNumber(rowPage.getPageNumber());
        page.setPageSize(rowPage.getPageSize());
        page.setTotalRow(rowPage.getTotalRow());
        page.setTotalPage(rowPage.getTotalPage());
        page.setRecords(convertRows(ctx, rowPage.getRecords()));
        return page;
    }

    @Override
    public List<Map<String, Object>> list(String formId, Map<String, String> params) {
        DevFormContext ctx = loadContext(formId);
        QueryWrapper wrapper = buildQueryWrapper(ctx, params);
        applySort(ctx, wrapper, params);
        return convertRows(ctx, Db.selectListByQuery(ctx.getTableName(), wrapper));
    }

    @Override
    public List<Map<String, Object>> tree(String formId, Map<String, String> params) {
        DevFormContext ctx = loadContext(formId);
        Assert.isTrue(ctx.isTreeList(), "当前表单不是树形列表");
        DevForm form = ctx.getForm();
        String parentColumn = resolveTreeParentColumn(form);
        String labelColumn = resolveTreeLabelColumn(ctx, form);
        String sortColumn = StrUtil.blankToDefault(form.getTreeSortField(), parentColumn);

        List<Map<String, Object>> rows = list(formId, params);
        String rootValue = StrUtil.blankToDefault(form.getTreeRootValue(), Constant.ROOT_ID);

        List<TreeNode<String>> nodes = rows.stream().map(row -> {
            TreeNode<String> node = new TreeNode<>();
            String id = Convert.toStr(row.get(DevFormContext.toFieldName(ctx.getPrimaryKeyColumn())));
            node.setId(id);
            node.setParentId(Convert.toStr(row.get(DevFormContext.toFieldName(parentColumn)), rootValue));
            node.setName(Convert.toStr(row.get(DevFormContext.toFieldName(labelColumn)), id));
            node.setWeight(Convert.toInt(row.get(DevFormContext.toFieldName(sortColumn)), 0));
            node.setExtra(new LinkedHashMap<>(row));
            return node;
        }).toList();

        List<Tree<String>> trees = TreeUtil.build(nodes, rootValue);
        return trees.stream().map(this::treeToMap).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getInfo(String formId, Serializable id) {
        DevFormContext ctx = loadContext(formId);
        QueryWrapper wrapper = QueryWrapper.create()
                .where(column(ctx.getPrimaryKeyColumn()).eq(id));
        if (ctx.hasLogicDelete()) {
            wrapper.and(column(ctx.getLogicDeleteColumn()).eq(false));
        }
        Row row = Db.selectOneByQuery(ctx.getTableName(), wrapper);
        Assert.notNull(row, "数据不存在");
        return convertRow(ctx, row);
    }

    @Override
    public boolean save(String formId, Map<String, Object> data) {
        DevFormContext ctx = loadContext(formId);
        Row row = buildRow(ctx, data, false);
        fillInsertDefaults(ctx, row);
        validateFormData(ctx, row, false);
        Assert.isTrue(Db.insert(ctx.getTableName(), row) > 0, "保存失败");
        return true;
    }

    @Override
    public boolean update(String formId, Map<String, Object> data) {
        DevFormContext ctx = loadContext(formId);
        Object id = data.get(DevFormContext.toFieldName(ctx.getPrimaryKeyColumn()));
        if (id == null) {
            id = data.get(ctx.getPrimaryKeyColumn());
        }
        Assert.notNull(id, "主键不能为空");
        Row row = buildRow(ctx, data, true);
        fillUpdateDefaults(ctx, row);
        validateFormData(ctx, row, true);
        QueryWrapper wrapper = QueryWrapper.create()
                .where(column(ctx.getPrimaryKeyColumn()).eq(id));
        Assert.isTrue(Db.updateByQuery(ctx.getTableName(), row, wrapper) > 0, "更新失败");
        return true;
    }

    @Override
    public boolean remove(String formId, Serializable id) {
        return removeBatch(formId, List.of(id));
    }

    @Override
    public boolean removeBatch(String formId, List<? extends Serializable> ids) {
        DevFormContext ctx = loadContext(formId);
        Assert.notEmpty(ids, "请选择要删除的数据");
        QueryWrapper wrapper = QueryWrapper.create()
                .where(column(ctx.getPrimaryKeyColumn()).in(ids));
        if (ctx.hasLogicDelete()) {
            Row row = new Row();
            row.set(ctx.getLogicDeleteColumn(), true);
            fillUpdateDefaults(ctx, row);
            Assert.isTrue(Db.updateByQuery(ctx.getTableName(), row, wrapper) > 0, "删除失败");
        } else {
            Assert.isTrue(Db.deleteByQuery(ctx.getTableName(), wrapper) > 0, "删除失败");
        }
        return true;
    }

    @Override
    @SneakyThrows
    public void export(String formId, Map<String, String> params) {
        DevFormContext ctx = loadContext(formId);
        List<DevFormItem> columns = ctx.listColumns();
        Assert.notEmpty(columns, "没有可导出的列");
        List<Map<String, Object>> rows = list(formId, params);

        File file = FileUtil.createTempFile(".xlsx", true);
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            List<String> headers = columns.stream()
                    .map(it -> StrUtil.blankToDefault(it.getColumnRemarks(), it.getColumnName()))
                    .toList();
            writer.writeHeadRow(headers);
            for (Map<String, Object> row : rows) {
                List<Object> line = columns.stream()
                        .map(it -> formatExportValue(it, row.get(DevFormContext.toFieldName(it.getColumnName()))))
                        .collect(Collectors.toList());
                writer.writeRow(line);
            }
            writer.flush(FileUtil.getOutputStream(file));
        }
        String fileName = StrUtil.blankToDefault(ctx.getForm().getRemarks(), ctx.getTableName())
                + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        WebUtil.renderFile(file, fileName);
    }

    @Override
    @SneakyThrows
    public void importTemplate(String formId) {
        DevFormContext ctx = loadContext(formId);
        List<DevFormItem> columns = ctx.importColumns();
        Assert.notEmpty(columns, "没有可导入的列");
        File file = FileUtil.createTempFile(".xlsx", true);
        try (ExcelWriter writer = ExcelUtil.getWriter(true)) {
            writer.writeHeadRow(columns.stream()
                    .map(it -> StrUtil.blankToDefault(it.getColumnRemarks(), it.getColumnName()))
                    .toList());
            writer.flush(FileUtil.getOutputStream(file));
        }
        WebUtil.renderFile(file, ctx.getTableName() + "_import_template.xlsx");
    }

    @Override
    @SneakyThrows
    public DevFormImportResultVO importData(String formId, MultipartFile file) {
        DevFormContext ctx = loadContext(formId);
        List<DevFormItem> columns = ctx.importColumns();
        Assert.notEmpty(columns, "没有可导入的列");
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<List<Object>> rows = reader.read();
        Assert.isTrue(CollUtil.size(rows) > 1, "导入文件无数据");

        List<DevFormImportResultVO.ImportError> errors = new ArrayList<>();
        int success = 0;
        for (int i = 1; i < rows.size(); i++) {
            List<Object> line = rows.get(i);
            if (CollUtil.isEmpty(line) || line.stream().allMatch(ObjUtil::isEmpty)) {
                continue;
            }
            try {
                Map<String, Object> data = new LinkedHashMap<>();
                for (int j = 0; j < columns.size(); j++) {
                    DevFormItem item = columns.get(j);
                    Object value = j < line.size() ? line.get(j) : null;
                    if (ObjUtil.isNotEmpty(value) && StrUtil.isNotBlank(item.getDictCode())) {
                        value = DictUtil.getValue(item.getDictCode(), Convert.toStr(value));
                    }
                    data.put(DevFormContext.toFieldName(item.getColumnName()), value);
                }
                save(formId, data);
                success++;
            } catch (Exception ex) {
                errors.add(DevFormImportResultVO.ImportError.builder()
                        .row(i + 1)
                        .message(ex.getMessage())
                        .build());
            }
        }
        int total = rows.size() - 1;
        return DevFormImportResultVO.builder()
                .total(total)
                .success(success)
                .fail(total - success)
                .errors(errors)
                .build();
    }

    private DevFormContext loadContext(String formId) {
        DevForm form = devFormService.getById(formId);
        Assert.notNull(form, "表单不存在");
        List<DevFormItem> items = devFormItemService.list(
                DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID.eq(formId));
        return new DevFormContext(form, items);
    }

    private DevFormTreeConfigVO buildTreeConfig(DevForm form) {
        if (!ListType.TREE.getValue().equals(form.getListType())) {
            return null;
        }
        return DevFormTreeConfigVO.builder()
                .parentField(DevFormContext.toFieldName(
                        StrUtil.blankToDefault(form.getTreeParentField(), "parent_id")))
                .labelField(DevFormContext.toFieldName(
                        StrUtil.blankToDefault(form.getTreeLabelField(), "name")))
                .sortField(StrUtil.isNotBlank(form.getTreeSortField())
                        ? DevFormContext.toFieldName(form.getTreeSortField()) : null)
                .rootValue(StrUtil.blankToDefault(form.getTreeRootValue(), Constant.ROOT_ID))
                .build();
    }

    private List<DevFormColumnSchemaVO> buildColumnSchemas(DevFormContext ctx) {
        return ctx.getItems().stream()
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .map(it -> DevFormColumnSchemaVO.builder()
                        .columnName(it.getColumnName())
                        .fieldName(DevFormContext.toFieldName(it.getColumnName()))
                        .label(StrUtil.blankToDefault(it.getColumnRemarks(), it.getColumnName()))
                        .fieldType(it.getFieldType())
                        .fieldAttribute(it.getFieldAttribute())
                        .showInList(Boolean.TRUE.equals(it.getIsShowList()))
                        .allowSort(Boolean.TRUE.equals(it.getIsAllowSort()))
                        .showInQuery(Boolean.TRUE.equals(it.getIsShowQuery()))
                        .queryComponent(StrUtil.blankToDefault(it.getQueryComponent(), "Input"))
                        .showInForm(Boolean.TRUE.equals(it.getIsShowForm()))
                        .formComponent(StrUtil.blankToDefault(it.getFormComponent(), inferComponent(it)))
                        .required(Boolean.TRUE.equals(it.getFormRequired()))
                        .readonly(Boolean.TRUE.equals(it.getIsReadonly())
                                || FieldAttribute.PRIMARY_KEY.getValue().equals(it.getFieldAttribute()))
                        .regexp(it.getFormRegexp())
                        .defaultValue(it.getColumnDefaultVal())
                        .dictCode(it.getDictCode())
                        .nullable(Boolean.TRUE.equals(it.getColumnNullable()))
                        .maxLength(it.getColumnLength())
                        .pointLength(it.getColumnPointLength())
                        .weight(it.getWeight())
                        .build())
                .toList();
    }

    private String inferComponent(DevFormItem item) {
        if (StrUtil.isNotBlank(item.getDictCode())) {
            return "Select";
        }
        String fieldType = StrUtil.blankToDefault(item.getFieldType(), "");
        if (fieldType.contains("Boolean")) {
            return "Switch";
        }
        if (fieldType.contains("Date")) {
            return fieldType.contains("Time") ? "DateTimePicker" : "DatePicker";
        }
        if (fieldType.contains("Integer") || fieldType.contains("Long")
                || fieldType.contains("Double") || fieldType.contains("BigDecimal")) {
            return "InputNumber";
        }
        return "Input";
    }

    private QueryWrapper buildQueryWrapper(DevFormContext ctx, Map<String, String> params) {
        QueryWrapper wrapper = QueryWrapper.create();
        if (ctx.hasLogicDelete()) {
            wrapper.and(column(ctx.getLogicDeleteColumn()).eq(false));
        }
        if (CollUtil.isEmpty(params)) {
            return wrapper;
        }
        params.forEach((key, value) -> {
            if (IGNORE_PARAMS.contains(key) || StrUtil.isBlank(value)) {
                return;
            }
            String columnName;
            try {
                columnName = ctx.resolveColumnName(key);
            } catch (Exception ex) {
                return;
            }
            DevFormItem item = ctx.getItem(columnName);
            if (item == null || !Boolean.TRUE.equals(item.getIsShowQuery())) {
                return;
            }
            String normalized = normalizeQueryValue(value);
            if (StrUtil.startWith(value, "*") || StrUtil.endWith(value, "*")) {
                wrapper.and(column(columnName).like(normalized));
            } else {
                wrapper.and(column(columnName).eq(normalized));
            }
        });
        return wrapper;
    }

    private QueryColumn column(String columnName) {
        return new QueryColumn(columnName);
    }

    private String normalizeQueryValue(String value) {
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

    private void applySort(DevFormContext ctx, QueryWrapper wrapper, Map<String, String> params) {
        if (CollUtil.isEmpty(params)) {
            wrapper.orderBy(ctx.getPrimaryKeyColumn(), false);
            return;
        }
        String sortField = params.get("sortField");
        if (StrUtil.isBlank(sortField)) {
            wrapper.orderBy(ctx.getPrimaryKeyColumn(), false);
            return;
        }
        String columnName = ctx.resolveColumnName(sortField);
        DevFormItem item = ctx.getItem(columnName);
        Assert.isTrue(item != null && Boolean.TRUE.equals(item.getIsAllowSort()), "字段不允许排序");
        boolean asc = !"desc".equalsIgnoreCase(params.get("sortOrder"));
        wrapper.orderBy(columnName, asc);
    }

    private List<Map<String, Object>> convertRows(DevFormContext ctx, List<Row> rows) {
        if (CollUtil.isEmpty(rows)) {
            return List.of();
        }
        return rows.stream().map(row -> convertRow(ctx, row)).collect(Collectors.toList());
    }

    private Map<String, Object> convertRow(DevFormContext ctx, Row row) {
        Map<String, Object> map = new LinkedHashMap<>();
        row.forEach((column, value) -> {
            if (!ctx.isDbColumn(column)) {
                return;
            }
            DevFormItem item = ctx.getItem(column);
            if (item != null && StrUtil.isNotBlank(item.getDictCode()) && value != null) {
                map.put(DevFormContext.toFieldName(column), value);
                map.put(DevFormContext.toFieldName(column) + "Label",
                        DictUtil.getLabel(item.getDictCode(), Convert.toStr(value)));
            } else {
                map.put(DevFormContext.toFieldName(column), value);
            }
        });
        return map;
    }

    private Row buildRow(DevFormContext ctx, Map<String, Object> data, boolean update) {
        Row row = new Row();
        data.forEach((fieldName, value) -> {
            if (IGNORE_PARAMS.contains(fieldName)) {
                return;
            }
            String columnName = ctx.resolveColumnName(fieldName);
            if (!ctx.isDbColumn(columnName)) {
                return;
            }
            if (update && ctx.isPrimaryKey(columnName)) {
                return;
            }
            DevFormItem item = ctx.getItem(columnName);
            if (item != null && FieldAttribute.DEL_FLAG.getValue().equals(item.getFieldAttribute())) {
                return;
            }
            if (update && AUDIT_COLUMNS.contains(columnName)
                    && !"update_by".equals(columnName) && !"update_date".equals(columnName)) {
                return;
            }
            if (!update && AUDIT_COLUMNS.contains(columnName)
                    && !"create_by".equals(columnName) && !"create_date".equals(columnName)) {
                return;
            }
            row.set(columnName, value);
        });
        return row;
    }

    private void fillInsertDefaults(DevFormContext ctx, Row row) {
        String pkColumn = ctx.getPrimaryKeyColumn();
        if (ObjUtil.isEmpty(row.get(pkColumn))) {
            row.set(pkColumn, IdUtil.getSnowflakeNextIdStr());
        }
        Date now = new Date();
        setIfColumnExists(ctx, row, "create_by", SecureUtil.getUsername());
        setIfColumnExists(ctx, row, "create_date", now);
        setIfColumnExists(ctx, row, "update_by", SecureUtil.getUsername());
        setIfColumnExists(ctx, row, "update_date", now);
        if (ctx.hasLogicDelete() && ObjUtil.isEmpty(row.get(ctx.getLogicDeleteColumn()))) {
            row.set(ctx.getLogicDeleteColumn(), false);
        }
        ctx.formColumns().forEach(item -> {
            if (ObjUtil.isEmpty(row.get(item.getColumnName()))
                    && StrUtil.isNotBlank(item.getColumnDefaultVal())) {
                row.set(item.getColumnName(), item.getColumnDefaultVal());
            }
        });
    }

    private void fillUpdateDefaults(DevFormContext ctx, Row row) {
        setIfColumnExists(ctx, row, "update_by", SecureUtil.getUsername());
        setIfColumnExists(ctx, row, "update_date", new Date());
    }

    private void setIfColumnExists(DevFormContext ctx, Row row, String columnName, Object value) {
        if (ctx.getColumnMap().containsKey(columnName)) {
            row.set(columnName, value);
        }
    }

    private void validateFormData(DevFormContext ctx, Row row, boolean update) {
        for (DevFormItem item : ctx.formColumns()) {
            if (update && ctx.isPrimaryKey(item.getColumnName())) {
                continue;
            }
            Object value = row.get(item.getColumnName());
            if (Boolean.TRUE.equals(item.getFormRequired()) && ObjUtil.isEmpty(value)) {
                throw new IllegalArgumentException(item.getColumnRemarks() + "不能为空");
            }
            if (ObjUtil.isNotEmpty(value) && StrUtil.isNotBlank(item.getFormRegexp())) {
                Assert.isTrue(Convert.toStr(value).matches(item.getFormRegexp()),
                        "{}格式不正确", item.getColumnRemarks());
            }
        }
    }

    private Object formatExportValue(DevFormItem item, Object value) {
        if (value == null) {
            return "";
        }
        if (StrUtil.isNotBlank(item.getDictCode())) {
            return DictUtil.getLabel(item.getDictCode(), Convert.toStr(value));
        }
        return value;
    }

    private String resolveTreeParentColumn(DevForm form) {
        return StrUtil.blankToDefault(form.getTreeParentField(), "parent_id");
    }

    private String resolveTreeLabelColumn(DevFormContext ctx, DevForm form) {
        if (StrUtil.isNotBlank(form.getTreeLabelField())) {
            return form.getTreeLabelField();
        }
        return ctx.formColumns().stream()
                .map(DevFormItem::getColumnName)
                .filter(name -> !ctx.isPrimaryKey(name))
                .findFirst()
                .orElse(ctx.getPrimaryKeyColumn());
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> treeToMap(Tree<String> tree) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (tree.get("extra") instanceof Map<?, ?> extra) {
            map.putAll((Map<String, Object>) extra);
        }
        map.put("id", tree.getId());
        map.put("name", tree.getName());
        map.put("parentId", tree.getParentId());
        if (CollUtil.isNotEmpty(tree.getChildren())) {
            map.put("children", tree.getChildren().stream().map(this::treeToMap).toList());
        }
        return map;
    }
}
