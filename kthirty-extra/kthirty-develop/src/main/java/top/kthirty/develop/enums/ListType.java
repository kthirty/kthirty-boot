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
public enum ListType {
    PAGE("1","分页列表"),
    LIST("2","不分页列表"),
    TREE("3","树形列表");

    private final String value;
    private final String description;
    /***
     * 匹配枚举
     * @author Kthirty
     * @since 2024/7/2
     * @param value 枚举值
     * @param defaultValue 默认类型
     * @return top.kthirty.develop.enums.ListType
     */
    public static ListType fromValue(String value,ListType defaultValue) {
        for (ListType type : ListType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return defaultValue;
    }
}
