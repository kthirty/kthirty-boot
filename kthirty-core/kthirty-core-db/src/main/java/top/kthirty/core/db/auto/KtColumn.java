package top.kthirty.core.db.auto;

import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@ColumnDefine
public @interface KtColumn {
    /**
     * 字段类型
     * @see ColumnSupport
     * @see Type
     */
    Type value();

    /**
     * 是否非空字段
     */
    boolean notNull() default false;
    /**
     * 字段备注
     */
    String comment() default "";


    enum Type{
        // 短字符串
        SHORT_STRING,
        // 常规字符串
        STRING,
        // 长字符串
        LONG_STRING,
        // 金额
        MONEY,
        // 小数
        DECIMAL,
        // 长小数
        LONG_DECIMAL,
        // 整数
        INTEGER,
        // 大文本
        TEXT,
        // 日期时间
        DATETIME,
        // 日期
        DATE,
        // 布尔值
        BOOLEAN,
        // 长整型
        LONG;
    }
}

