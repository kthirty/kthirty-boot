package top.kthirty.core.tool.jackson.generate;

import java.lang.annotation.*;

/**
 * @description 将对象中的字段拼接生成一个新的字段
 * @author KThirty
 * @since 2024/6/14 16:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface GenerateFields {
    GenerateField[] value() default {};
}
