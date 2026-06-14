package top.kthirty.develop.util;

import cn.hutool.core.util.StrUtil;

/**
 * DevForm 命名工具
 */
public final class DevFormNameUtil {

    private DevFormNameUtil() {
    }

    public static String toEntityName(String tableName) {
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
    }

    public static String toEntityVar(String tableName) {
        return StrUtil.toCamelCase(tableName);
    }

    public static String toFieldName(String columnName) {
        return StrUtil.toCamelCase(columnName);
    }

    public static String toApiPrefix(String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return "/dev/form";
        }
        String[] parts = tableName.split("_", 2);
        if (parts.length == 2) {
            return "/" + parts[0] + "/" + parts[1].replace("_", "");
        }
        return "/" + tableName;
    }

    public static String toFrontendPath(String tableName, String frontendModule) {
        String module = StrUtil.blankToDefault(frontendModule, "develop");
        String[] parts = tableName.split("_", 2);
        if (parts.length == 2 && module.equals(parts[0])) {
            return module + "/" + parts[1];
        }
        return module + "/" + tableName.replace("_", "/");
    }
}
