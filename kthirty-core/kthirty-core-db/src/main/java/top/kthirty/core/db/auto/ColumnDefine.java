package top.kthirty.core.db.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 数据库字段标注，配合auto-table实现自动建表
 * @author KThirty
 * @since 2025/4/2 9:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnDefine {
    /**
     * 字段类型
     * @see ColumnSupport
     * @see Type
     */
    Type value();


    enum Type{
        /**
         * 字符串 32
         */
        SHORT_STRING,
        /**
         * 字符串255长度
         */
        STRING,
        /**
         * 字符串2000长度
         */
        LONG_STRING,
        /**
         * 金额(20,2)
         */
        MONEY,
        /**
         * 小数(10,2)
         */
        DECIMAL,
        /**
         * 小数(20,4)
         */
        LONG_DECIMAL,
        /**
         * 整数11
         */
        INTEGER,
        /**
         * 大文本 text/clob
         */
        TEXT,
        /**
         * 日期时间timestamp
         */
        DATETIME,
        /**
         * 布尔值
         */
        BOOLEAN,
        /**
         * 长整型
         */
        LONG,
        JSON
    }
}

