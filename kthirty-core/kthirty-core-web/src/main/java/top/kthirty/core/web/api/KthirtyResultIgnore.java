package top.kthirty.core.web.api;

import java.lang.annotation.*;

/**
 * 忽略系统自带的参数响应处理
 * @author KThirty
 * @date Created in 2020/9/28 15:03
 * <p>
 *     当方法或类上存在 @KthirtyResultIgnore 注解时，接口会将忽略@KthirtyResult的作用
 * </p>
 * @see KthirtyResult
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface KthirtyResultIgnore {
}
