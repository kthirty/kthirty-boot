package top.kthirty.develop.util;

import cn.hutool.db.meta.JdbcType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
/**
 * @description Jdbc与Java类型转换
 * @author KThirty
 * @since 2025/6/27 16:08
 */
public class JdbcTypeToJavaType {

    private static final Map<JdbcType, Class<?>> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put(JdbcType.BIT, Boolean.class);
        TYPE_MAP.put(JdbcType.TINYINT, Integer.class);
        TYPE_MAP.put(JdbcType.SMALLINT, Integer.class);
        TYPE_MAP.put(JdbcType.INTEGER, Integer.class);
        TYPE_MAP.put(JdbcType.BIGINT, Long.class);
        TYPE_MAP.put(JdbcType.FLOAT, Float.class);
        TYPE_MAP.put(JdbcType.REAL, Float.class);
        TYPE_MAP.put(JdbcType.DOUBLE, Double.class);
        TYPE_MAP.put(JdbcType.NUMERIC, BigDecimal.class);
        TYPE_MAP.put(JdbcType.DECIMAL, BigDecimal.class);
        TYPE_MAP.put(JdbcType.CHAR, String.class);
        TYPE_MAP.put(JdbcType.VARCHAR, String.class);
        TYPE_MAP.put(JdbcType.LONGVARCHAR, String.class);
        TYPE_MAP.put(JdbcType.DATE, Date.class);
        TYPE_MAP.put(JdbcType.TIME, Date.class);
        TYPE_MAP.put(JdbcType.TIMESTAMP, Date.class);
        TYPE_MAP.put(JdbcType.BINARY, byte[].class);
        TYPE_MAP.put(JdbcType.VARBINARY, byte[].class);
        TYPE_MAP.put(JdbcType.LONGVARBINARY, byte[].class);
        TYPE_MAP.put(JdbcType.BLOB, Blob.class);
        TYPE_MAP.put(JdbcType.CLOB, Clob.class);
        TYPE_MAP.put(JdbcType.BOOLEAN, Boolean.class);
        TYPE_MAP.put(JdbcType.NVARCHAR, String.class);
        TYPE_MAP.put(JdbcType.NCHAR, String.class);
        TYPE_MAP.put(JdbcType.NCLOB, Clob.class);
        TYPE_MAP.put(JdbcType.ROWID, String.class);
        TYPE_MAP.put(JdbcType.LONGNVARCHAR, String.class);
        TYPE_MAP.put(JdbcType.SQLXML, SQLXML.class);
        TYPE_MAP.put(JdbcType.TIMESTAMP_WITH_TIMEZONE, Timestamp.class);
        TYPE_MAP.put(JdbcType.TIME_WITH_TIMEZONE, Time.class);

        // 可选：为未定义类型做兜底
        TYPE_MAP.put(JdbcType.UNDEFINED, Object.class);
        TYPE_MAP.put(JdbcType.OTHER, Object.class);
    }

    public static Class<?> toJavaType(JdbcType jdbcType) {
        return TYPE_MAP.getOrDefault(jdbcType, Object.class);
    }

    public static String toJavaTypeName(JdbcType jdbcType) {
        return toJavaType(jdbcType).getSimpleName();
    }
}
