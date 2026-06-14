package top.kthirty.develop.codegen;

import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.util.DevFormNameUtil;

/**
 * 代码生成字段模型
 */
@Data
@Builder
public class DevFormCodegenField {

    private String fieldName;
    private String listFieldName;
    private String label;
    private String javaType;
    private String component;
    private String dictCode;
    private boolean required;
    private String defaultValue;
    private boolean hasDict;

    public static DevFormCodegenField from(DevFormItem item) {
        String fieldName = DevFormNameUtil.toFieldName(item.getColumnName());
        boolean hasDict = StrUtil.isNotBlank(item.getDictCode());
        return DevFormCodegenField.builder()
                .fieldName(fieldName)
                .listFieldName(hasDict ? fieldName + "Label" : fieldName)
                .label(escapeJs(StrUtil.blankToDefault(item.getColumnRemarks(), item.getColumnName())))
                .javaType(simpleType(item.getFieldType()))
                .component(mapComponent(item))
                .dictCode(item.getDictCode())
                .required(Boolean.TRUE.equals(item.getFormRequired()))
                .defaultValue(escapeJs(item.getColumnDefaultVal()))
                .hasDict(hasDict)
                .build();
    }

    private static String mapComponent(DevFormItem item) {
        if (StrUtil.isNotBlank(item.getFormComponent())) {
            return switch (item.getFormComponent()) {
                case "Radio" -> "RadioGroup";
                case "DateTimePicker" -> "DatePicker";
                default -> item.getFormComponent();
            };
        }
        if (StrUtil.isNotBlank(item.getDictCode())) {
            return "Select";
        }
        return "Input";
    }

    private static String simpleType(String fieldType) {
        if (StrUtil.isBlank(fieldType)) {
            return "String";
        }
        int idx = fieldType.lastIndexOf('.');
        return idx >= 0 ? fieldType.substring(idx + 1) : fieldType;
    }

    private static String escapeJs(String text) {
        if (StrUtil.isBlank(text)) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("'", "\\'");
    }
}
