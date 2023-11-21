package top.kthirty.core.web.api;

import java.lang.annotation.*;

/**
 * <p>
 *     当方法或类上存在 @KthirtyResult 注解时，系统会将非<code>top.kthirty.core.tool.api.R</code>类型的响应参数
 *     封装为<code>top.kthirty.core.tool.api.R</code>类型
 *     例：方法响应参数为 Object 类型的 o，则实际响应信息为R.success(o)
 * </p>
 * @author KThirty
 * @since 2020/9/28 15:03
 * @see KthirtyResultIgnore,KthirtyResponseBodyAdvice
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface KthirtyResult {
}
