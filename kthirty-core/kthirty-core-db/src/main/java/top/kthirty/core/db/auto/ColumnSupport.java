package top.kthirty.core.db.auto;

import cn.hutool.core.map.FuncKeyMap;
import cn.hutool.core.map.multi.RowKeyTable;
import lombok.AllArgsConstructor;
import lombok.Data;

import com.mybatisflex.core.dialect.DbType;

import cn.hutool.core.lang.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KThirty
 * @description 字段类型
 * @since 2025/4/2 9:45
 */
public class ColumnSupport {
    private static final RowKeyTable<DbType, KtColumn.Type, ColumnInfo> TYPE = new RowKeyTable<>();

    static {
        // Oracle
        TYPE.put(DbType.ORACLE, KtColumn.Type.SHORT_STRING, new ColumnInfo("varchar2", 32, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.STRING, new ColumnInfo("varchar2", 100, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.LONG_STRING, new ColumnInfo("varchar2", 500, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.MONEY, new ColumnInfo("number", 20, 2));
        TYPE.put(DbType.ORACLE, KtColumn.Type.DECIMAL, new ColumnInfo("number", 10, 2));
        TYPE.put(DbType.ORACLE, KtColumn.Type.LONG_DECIMAL, new ColumnInfo("number", 20, 4));
        TYPE.put(DbType.ORACLE, KtColumn.Type.INTEGER, new ColumnInfo("number", 11, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.TEXT, new ColumnInfo("clob", -1, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.DATETIME, new ColumnInfo("timestamp", 3, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.BOOLEAN, new ColumnInfo("number", 1, -1));
        TYPE.put(DbType.ORACLE, KtColumn.Type.LONG, new ColumnInfo("number", 20, -1));

        // Mysql
        TYPE.put(DbType.MYSQL, KtColumn.Type.SHORT_STRING, new ColumnInfo("varchar", 32, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.STRING, new ColumnInfo("varchar", 100, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.LONG_STRING, new ColumnInfo("varchar", 500, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.MONEY, new ColumnInfo("decimal", 20, 2));
        TYPE.put(DbType.MYSQL, KtColumn.Type.DECIMAL, new ColumnInfo("decimal", 10, 2));
        TYPE.put(DbType.MYSQL, KtColumn.Type.LONG_DECIMAL, new ColumnInfo("decimal", 20, 4));
        TYPE.put(DbType.MYSQL, KtColumn.Type.INTEGER, new ColumnInfo("int", 11, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.TEXT, new ColumnInfo("text", -1, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.DATETIME, new ColumnInfo("TIMESTAMP", 3, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.BOOLEAN, new ColumnInfo("tinyint", 1, -1));
        TYPE.put(DbType.MYSQL, KtColumn.Type.LONG, new ColumnInfo("bigint", 20, -1));
    }

    public static ColumnInfo getColumnType(DbType dbType,KtColumn.Type type) {
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
