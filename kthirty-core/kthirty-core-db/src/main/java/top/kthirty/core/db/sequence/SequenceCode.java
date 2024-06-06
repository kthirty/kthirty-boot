package top.kthirty.core.db.sequence;

import top.kthirty.core.db.sequence.handler.RuleHandler;

import java.lang.annotation.*;

/**
 * 序列号自动填充
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SequenceCode {
    /**
     * 规则处理器
     */
    Class<? extends RuleHandler> handler();

    /**
     * 处理器参数
     */
    String[] handlerParams() default {};

    /**
     * 是否每次重启后的第一次都需要查询数据库中所有序列号并重建索引
     */
    boolean rebuildCache() default false;
}
