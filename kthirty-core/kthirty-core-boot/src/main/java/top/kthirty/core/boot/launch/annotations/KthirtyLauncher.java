package top.kthirty.core.boot.launch.annotations;

import java.lang.annotation.*;
/**
 * <p>
 * 自定义Launcher注解
 * </p>
 *
 * @author KThirty
 * @since 2023/11/16
 */
@Repeatable(KthirtyLaunchers.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KthirtyLauncher {
    String value();
}
