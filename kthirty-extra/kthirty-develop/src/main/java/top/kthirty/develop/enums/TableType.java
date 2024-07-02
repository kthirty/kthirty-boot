package top.kthirty.develop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 代码生成的列表类型
 * </p>
 *
 * @author KThirty
 * @since 2024/7/2
 */
@Getter
@AllArgsConstructor
public enum TableType {
    PAGE("1","单表"),
    LIST("2","主表"),
    TREE("3","附表");

    private final String value;
    private final String description;
    /***
     * 匹配枚举
     * @author Kthirty
     * @since 2024/7/2
     * @param value 枚举值
     * @param defaultValue 默认类型
     * @return top.kthirty.develop.enums.TableType
     */
    public static TableType fromValue(String value, TableType defaultValue) {
        for (TableType type : TableType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return defaultValue;
    }
}
