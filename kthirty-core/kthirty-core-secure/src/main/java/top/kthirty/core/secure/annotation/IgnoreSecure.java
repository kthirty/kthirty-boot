package top.kthirty.core.secure.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 禁用拦截器
 * </p>
 *
 * @author KThirty
 * @since 2023/2/9
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IgnoreSecure {
    /**
     * 禁用请求拦截器
     * @return boolean
     */
    boolean request() default true;

    /**
     * 禁用权限拦截器
     * @return boolean
     */
    boolean permission() default true;
}
