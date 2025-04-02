package top.kthirty.core.db.auto;

import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import org.dromara.autotable.annotation.ColumnComment;
import org.dromara.autotable.annotation.ColumnNotNull;
import org.dromara.autotable.annotation.ColumnType;
import org.springframework.core.annotation.AliasFor;

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
@ColumnNotNull
@ColumnComment("")
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
    @AliasFor(annotation = ColumnNotNull.class, attribute = "value")
    boolean notNull() default false;
    /**
     * 字段备注
     */
    @AliasFor(annotation = ColumnComment.class, attribute = "value")
    String comment() default "";


    enum Type{
        /**
         * 字符串 32
         */
        SHORT_STRING,
        // 常规字符串 - 100
        STRING,
        // 长字符串 - 2000
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
        // 布尔值
        BOOLEAN,
        // 长整型
        LONG
    }
}

