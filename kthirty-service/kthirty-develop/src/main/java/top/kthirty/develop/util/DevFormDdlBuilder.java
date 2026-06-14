package top.kthirty.develop.util;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.dialect.DbType;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.enums.FieldAttribute;

/**
 * DDL 构建工具
 */
public final class DevFormDdlBuilder {

    private DevFormDdlBuilder() {
    }

    public static String buildColumnDefinition(DbType dbType, DevFormItem item) {
        StringBuilder sb = new StringBuilder();
        sb.append(quote(dbType, item.getColumnName())).append(" ");
        sb.append(buildColumnType(item));
        if (!Boolean.TRUE.equals(item.getColumnNullable())
                && !FieldAttribute.PRIMARY_KEY.getValue().equals(item.getFieldAttribute())) {
            sb.append(" NOT NULL");
        }
        if (StrUtil.isNotBlank(item.getColumnDefaultVal())) {
            sb.append(" DEFAULT ").append(formatDefault(item));
        }
        if (StrUtil.isNotBlank(item.getColumnRemarks())) {
            sb.append(" COMMENT '").append(escapeComment(item.getColumnRemarks())).append("'");
        }
        return sb.toString();
    }

    public static String buildAddColumnSql(DbType dbType, String tableName, DevFormItem item) {
        return "ALTER TABLE " + quote(dbType, tableName) + " ADD COLUMN " + buildColumnDefinition(dbType, item);
    }

    public static String buildCreateIndexSql(DbType dbType, String tableName, String indexName,
                                             String indexType, String columnNames) {
        String unique = "UNIQUE".equalsIgnoreCase(indexType) ? "UNIQUE " : "";
        String cols = StrUtil.splitTrim(columnNames, ',').stream()
                .map(it -> StrUtil.subBefore(it, ' ', false))
                .map(it -> quote(dbType, it.trim()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        return StrUtil.format("CREATE {}INDEX {} ON {} ({})",
                unique, quote(dbType, indexName), quote(dbType, tableName), cols);
    }

    private static String buildColumnType(DevFormItem item) {
        String type = StrUtil.blankToDefault(item.getColumnType(), "varchar").toLowerCase();
        Integer len = item.getColumnLength();
        Integer point = item.getColumnPointLength();
        if (len != null && len > 0) {
            if (point != null && point > 0 && (type.contains("decimal") || type.contains("number") || type.contains("numeric"))) {
                return type + "(" + len + "," + point + ")";
            }
            if (type.contains("varchar") || type.contains("char") || type.contains("int")
                    || type.contains("tinyint") || type.contains("bigint")) {
                return type + "(" + len + ")";
            }
        }
        return type;
    }

    private static String formatDefault(DevFormItem item) {
        String val = item.getColumnDefaultVal();
        String type = StrUtil.blankToDefault(item.getColumnType(), "").toLowerCase();
        if (type.contains("char") || type.contains("text") || type.contains("date") || type.contains("time")) {
            return "'" + val.replace("'", "''") + "'";
        }
        if ("CURRENT_TIMESTAMP".equalsIgnoreCase(val) || "now()".equalsIgnoreCase(val)) {
            return "CURRENT_TIMESTAMP";
        }
        return val;
    }

    private static String quote(DbType dbType, String name) {
        if (dbType == DbType.MYSQL) {
            return "`" + name + "`";
        }
        return "\"" + name + "\"";
    }

    private static String escapeComment(String comment) {
        return comment.replace("'", "''");
    }
}
