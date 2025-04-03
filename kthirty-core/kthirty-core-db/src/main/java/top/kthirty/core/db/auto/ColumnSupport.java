package top.kthirty.core.db.auto;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.multi.RowKeyTable;
import com.mybatisflex.core.dialect.DbType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author KThirty
 * @description 字段类型
 * @since 2025/4/2 9:45
 */
public class ColumnSupport {
    private static final RowKeyTable<DbType, ColumnDefine.Type, ColumnInfo> TYPE = new RowKeyTable<>();

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
