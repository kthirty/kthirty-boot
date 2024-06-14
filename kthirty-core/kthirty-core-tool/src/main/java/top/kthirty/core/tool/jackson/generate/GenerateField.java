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
public @interface GenerateField {
    /**
     * 生成的新字段名
     */
    String genField();

    /**
     * 对象中的字段名
     */
    String objField();
}
