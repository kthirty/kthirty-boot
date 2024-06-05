package top.kthirty.core.db.listener;

import top.kthirty.core.tool.utils.RuleCodeUtil;

import java.lang.annotation.*;

/**
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SequenceCode {
    /**
     * 处理器
     */
    Class<? extends RuleCodeUtil.Handler> handler();

    /**
     * 获取前缀的SQL
     */
    String appendPrefixSql() default "select {column} as prefix from {table} where id = '{parentId}'";

    /**
     * 是否需要拼接code前缀
     */
    boolean appendPrefix() default false;
}
