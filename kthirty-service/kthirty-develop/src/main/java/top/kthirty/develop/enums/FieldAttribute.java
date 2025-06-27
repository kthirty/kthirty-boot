package top.kthirty.develop.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @description 字段属性
 * @author KThirty
 * @since 2024/12/10 13:36
 */
@AllArgsConstructor
@Getter
public enum FieldAttribute {
    NORMAL("1","普通字段"),
    PRIMARY_KEY("2","主键"),
    DEL_FLAG("3","删除标记"),
    NOT_DB_FIELD("4","非数据库字段");

    private final String value;
    private final String description;

    public static FieldAttribute infer(String name, Set<String> pkNames) {
        if(pkNames.contains(name)){
            return PRIMARY_KEY;
        }
        if ("DELETED".equalsIgnoreCase(name)) {
            return DEL_FLAG;
        }
        return NORMAL;
    }

    public boolean isNormal(){
        return this == NORMAL;
    }
}
