package top.kthirty.core.db.auto;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.multi.RowKeyTable;
import com.mybatisflex.core.dialect.DbType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KThirty
 * @description 字段类型
 * @since 2025/4/2 9:45
 */
public class ColumnSupport {
    public static final RowKeyTable<DbType, ColumnDefine.Type, ColumnInfo> TYPE = new RowKeyTable<>();
    public static final Map<DbType, String[]> COLUMN_TYPES = new HashMap<>();

    static {
        // 初始化COLUMN_TYPES映射
        COLUMN_TYPES.put(DbType.ORACLE, new String[]{
                // 可变长度字符串
                "varchar2",
                // 固定长度字符串
                "char",
                // 数值类型（整数、小数等）
                "number",
                // 日期时间（含年月日时分秒）
                "date",
                // 精确时间戳
                "timestamp",
                // 大文本
                "clob",
                // 二进制大对象
                "blob",
                // 长文本（过时，不推荐）
                "long",
                // 浮点数（number 的别名）
                "float",
                // 支持多语言字符的可变长度字符串
                "nvarchar2",
                // 支持多语言字符的固定长度字符串
                "nchar"});
        COLUMN_TYPES.put(DbType.MYSQL, new String[]{
                // 整数
                "int",
                // 长整数
                "bigint",
                // 高精度数字
                "decimal",
                // 单精度浮点数
                "float",
                // 双精度浮点数
                "double",
                // 固定长度字符串
                "char",
                // 可变长度字符串
                "varchar",
                // 文本
                "text",
                // 日期
                "date",
                // 日期时间
                "datetime",
                // 时间戳
                "timestamp",
                // 时间（不含日期）
                "time",
                // 年份
                "year",
                // 布尔值
                "boolean",
                // 二进制数据
                "blob"
        });

    }

    static {
        // Oracle
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.SHORT_STRING, new ColumnInfo("varchar2", 32, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.STRING, new ColumnInfo("varchar2", 255, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.LONG_STRING, new ColumnInfo("varchar2", 2000, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.MONEY, new ColumnInfo("number", 20, 2));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.DECIMAL, new ColumnInfo("number", 10, 2));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.LONG_DECIMAL, new ColumnInfo("number", 20, 4));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.INTEGER, new ColumnInfo("number", -1, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.TEXT, new ColumnInfo("clob", -1, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.DATETIME, new ColumnInfo("timestamp", 3, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.BOOLEAN, new ColumnInfo("number", 1, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.LONG, new ColumnInfo("number", 20, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.JSON, new ColumnInfo("clob", -1, -1));
        TYPE.put(DbType.ORACLE, ColumnDefine.Type.SHORT_INTEGER, new ColumnInfo("number", 5, -1));
        // Mysql
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.SHORT_STRING, new ColumnInfo("varchar", 32, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.STRING, new ColumnInfo("varchar", 255, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.LONG_STRING, new ColumnInfo("varchar", 2000, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.MONEY, new ColumnInfo("decimal", 20, 2));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.DECIMAL, new ColumnInfo("decimal", 10, 2));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.LONG_DECIMAL, new ColumnInfo("decimal", 20, 4));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.INTEGER, new ColumnInfo("int", -1, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.TEXT, new ColumnInfo("text", -1, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.DATETIME, new ColumnInfo("timestamp", 3, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.BOOLEAN, new ColumnInfo("tinyint", 1, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.LONG, new ColumnInfo("bigint", 20, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.JSON, new ColumnInfo("json", -1, -1));
        TYPE.put(DbType.MYSQL, ColumnDefine.Type.SHORT_INTEGER, new ColumnInfo("smallint", -1, -1));

    }

    public static Map<ColumnDefine.Type, ColumnInfo> getRow(DbType dbType) {
        return TYPE.getRow(dbType);
    }

    public static ColumnInfo getColumnType(DbType dbType, ColumnDefine.Type type) {
        return Assert.notNull(TYPE.get(dbType, type), "未找到对应的数据类型{}", type);
    }

    @AllArgsConstructor
    @Data
    public static class ColumnInfo {
        private final String type;
        private final int length;
        private final int decimalLength;
    }
}
