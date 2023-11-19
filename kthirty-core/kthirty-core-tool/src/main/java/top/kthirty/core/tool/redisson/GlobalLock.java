package top.kthirty.core.tool.redisson;

import java.lang.annotation.*;

/**
 * <p>
 *  分布式锁（Redission实现）
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface GlobalLock {
    /**
     * 分布式锁名称
     * @return name
     */
    String value();

    long leaseTime() default 30;
}
