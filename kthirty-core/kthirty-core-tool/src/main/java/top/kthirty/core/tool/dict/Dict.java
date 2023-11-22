package top.kthirty.core.tool.dict;

import java.lang.annotation.*;
/**
 * <p>
 * 数据字典标识
 * 支持方法与字段标注
 * 要求Class实现DictFiller
 * </p>
 * @see top.kthirty.core.tool.dict.DictFiller
 * @author KThirty
 * @since 2023/11/22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Dict {
    /**
     * 数据字典code
     * 支持取数据库中信息，使用 表名:值对应的字段名:需要显示的字段名
     */
    String code() default "";

    /**
     * 生成的字段名
     * {}填充当前字段名
     */
    String fieldName() default "{}Label";

    /**
     * value 分割符用于多个值 为空不分割
     */
    String splitBy() default ",";
}
