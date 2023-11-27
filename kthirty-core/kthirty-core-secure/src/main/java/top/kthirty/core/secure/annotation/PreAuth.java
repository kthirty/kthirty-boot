package top.kthirty.core.secure.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>
 * 权限注解 用于检查权限 规定访问权限
 * 例: hasRole('admin') 包含admin角色
 * </p>
 *
 * @see top.kthirty.core.secure.auth.AuthFun
 * @author KThirty
 * @since 2021/9/7
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {
	/**
	 * 包含权限 sys:user:see等同于hasAllPermission('sys:user:see')
	 */
	@AliasFor(attribute = "all")
	String[] value() default {};

	/**
	 * 包含任意权限，优先级低于all
	 */
	String[] any() default {};

	/**
	 * 包含所有权限，优先级高于any
	 */
	@AliasFor(attribute = "value")
	String[] all() default {};
	/**
	 *
	 * Spring el
	 * 文档地址：https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#expressions-operators-logical
	 * 例: hasRole('admin') 包含admin角色
	 * 例: hasPermission('sys:user:see') 包含sys:user:see权限
	 * @see top.kthirty.core.secure.auth.AuthFun
	 * 优先级最低
	 */
	String el() default "";
}

