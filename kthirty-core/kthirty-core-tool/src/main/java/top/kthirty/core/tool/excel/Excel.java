package top.kthirty.core.tool.excel;

import top.kthirty.core.tool.excel.support.ExcelGroup;

import java.lang.annotation.*;

/**
 * <p>
 * Excel导入导出注解
 * </p>
 *
 * @author KThirty
 * @since 2023/11/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Excel {
    /**
     * 标题,实际标题按照以下顺序获取
     * 1. title
     * 2. @Schema 注解title
     * 3. 字段名称
     */
    String title() default "";

    /**
     * 单元格宽度
     */
    int width() default 200;

    /**
     * 导入导出分组
     * IMPORT导入
     * EXPORT导出
     * 也可自定义
     */
    String[] group() default {ExcelGroup.IMPORT,ExcelGroup.EXPORT};

}

