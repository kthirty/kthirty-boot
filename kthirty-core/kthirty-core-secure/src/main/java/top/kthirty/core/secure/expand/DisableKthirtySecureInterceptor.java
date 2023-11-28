package top.kthirty.core.secure.expand;


import top.kthirty.core.boot.launch.annotations.KthirtyLauncher;

import java.lang.annotation.*;

/**
 * <p>
 * 禁用鉴权拦截器，仅使用aspect拦截
 * </p>
 *
 * @author KThirty
 * @since 2022/9/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@KthirtyLauncher("top.kthirty.core.secure.expand.DisableKthirtySecureInterceptorLauncherImpl")
public @interface DisableKthirtySecureInterceptor { }
