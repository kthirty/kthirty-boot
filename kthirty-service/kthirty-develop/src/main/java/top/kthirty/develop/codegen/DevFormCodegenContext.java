package top.kthirty.develop.codegen;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.enums.FieldAttribute;
import top.kthirty.develop.enums.ListType;
import top.kthirty.develop.model.DevFormCodegenOption;
import top.kthirty.develop.util.DevFormNameUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成模板上下文
 */
@Data
@Builder
public class DevFormCodegenContext {

    private String entityName;
    private String entityVar;
    private String tableName;
    private String title;
    private String apiPrefix;
    private String frontendPath;
    private String backendPackage;
    private boolean treeList;

    private List<DevFormCodegenField> entityFields;
    private List<DevFormCodegenField> queryFields;
    private List<DevFormCodegenField> listFields;
    private List<DevFormCodegenField> formFields;

    private String nameField;
    private String treeParentField;
    private String treeRowField;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("entityName", entityName);
        map.put("entityVar", entityVar);
        map.put("tableName", tableName);
        map.put("title", title);
        map.put("apiPrefix", apiPrefix);
        map.put("frontendPath", frontendPath);
        map.put("backendPackage", backendPackage);
        map.put("treeList", treeList);
        map.put("entityFields", entityFields);
        map.put("queryFields", queryFields);
        map.put("listFields", listFields);
        map.put("formFields", formFields);
        map.put("nameField", nameField);
        map.put("treeParentField", treeParentField);
        map.put("treeRowField", treeRowField);
        return map;
    }

    public static DevFormCodegenContext from(DevForm form, DevFormCodegenOption option) {
        String entityName = DevFormNameUtil.toEntityName(form.getTableName());
        String entityVar = DevFormNameUtil.toEntityVar(form.getTableName());
        String apiPrefix = DevFormNameUtil.toApiPrefix(form.getTableName());
        String frontendPath = DevFormNameUtil.toFrontendPath(form.getTableName(), option.getFrontendModule());
        boolean treeList = ListType.TREE.getValue().equals(form.getListType());

        List<DevFormItem> businessItems = form.getItems().stream()
                .filter(it -> !FieldAttribute.NOT_DB_FIELD.getValue().equals(it.getFieldAttribute()))
                .filter(it -> !FieldAttribute.DEL_FLAG.getValue().equals(it.getFieldAttribute()))
                .filter(it -> !FieldAttribute.PRIMARY_KEY.getValue().equals(it.getFieldAttribute()))
                .filter(it -> !List.of("create_by", "update_by", "create_date", "update_date", "deleted")
                        .contains(it.getColumnName()))
                .collect(Collectors.toList());

        List<DevFormCodegenField> entityFields = businessItems.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowForm()) || Boolean.TRUE.equals(it.getIsShowList()))
                .map(DevFormCodegenField::from)
                .collect(Collectors.toList());

        List<DevFormCodegenField> queryFields = businessItems.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowQuery()))
                .map(DevFormCodegenField::from)
                .collect(Collectors.toList());

        List<DevFormCodegenField> listFields = businessItems.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowList()))
                .map(DevFormCodegenField::from)
                .collect(Collectors.toList());

        List<DevFormCodegenField> formFields = businessItems.stream()
                .filter(it -> Boolean.TRUE.equals(it.getIsShowForm()))
                .map(DevFormCodegenField::from)
                .collect(Collectors.toList());

        String nameField = listFields.isEmpty() ? "id"
                : listFields.get(0).getFieldName();
        String treeParentField = treeList
                ? DevFormNameUtil.toFieldName(StrUtil.blankToDefault(form.getTreeParentField(), "parent_id"))
                : null;

        return DevFormCodegenContext.builder()
                .entityName(entityName)
                .entityVar(entityVar)
                .tableName(form.getTableName())
                .title(escapeJs(form.getRemarks() == null ? entityName : form.getRemarks()))
                .apiPrefix(apiPrefix)
                .frontendPath(frontendPath)
                .backendPackage(option.getBackendPackage())
                .treeList(treeList)
                .entityFields(entityFields)
                .queryFields(queryFields)
                .listFields(listFields)
                .formFields(formFields)
                .nameField(nameField)
                .treeParentField(treeParentField)
                .treeRowField("id")
                .build();
    }

    private static String escapeJs(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("'", "\\'");
    }
}
