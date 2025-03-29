package top.kthirty.core.db.fill;



import com.tangzc.mybatisflex.annotation.FieldFill;

import java.lang.annotation.*;
/**
 * <p>
 * 字段填充
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FillData {
    Class<? extends FillHandler<?>> value();

    boolean override() default false;

    /**
     * 字段填充时机，默认只有插入逻辑触发
     */
    Scope scope() default Scope.INSERT_UPDATE;

    /**
     * 额外参数，提供给FillHandler
     */
    String[] args() default {};

    enum Scope {
        INSERT, UPDATE,
        /**
         * 插入和更新
         */
        INSERT_UPDATE
    }
}

