package top.kthirty.core.secure.aop;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import top.kthirty.core.secure.annotation.IgnoreSecure;
import top.kthirty.core.secure.annotation.PreAuth;
import top.kthirty.core.secure.auth.AuthConstant;
import top.kthirty.core.secure.auth.AuthFun;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.tool.exception.SecureException;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.core.tool.utils.StringUtil;
import top.kthirty.core.web.utils.ClassUtil;

import java.lang.reflect.Method;

/**
 * AOP 鉴权
 *
 * @author Kthirty
 */
@Aspect
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "kthirty.secure.enabled", matchIfMissing = true)
public class AuthAspect implements ApplicationContextAware {

	/**
	 * 表达式处理
	 */
	private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

	/**
	 * 切 方法 和 类上的 @PreAuth 注解
	 *
	 * @param point 切点
	 * @return Object
	 * @throws Throwable 没有权限的异常
	 */
	@Around(
		"@annotation(top.kthirty.core.secure.annotation.PreAuth) || " +
			"@within(top.kthirty.core.secure.annotation.PreAuth)"
	)
	public Object preAuth(ProceedingJoinPoint point) throws Throwable {
		if (handleAuth(point)) {
			return point.proceed();
		}
		throw new SecureException(SystemResultCode.UN_AUTHORIZED);
	}

	/**
	 * 处理权限
	 *
	 * @param point 切点
	 */
	private boolean handleAuth(ProceedingJoinPoint point) {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		// 禁用权限拦截器,直接跳过
		IgnoreSecure ignoreSecure = ClassUtil.getAnnotation(method, IgnoreSecure.class);
		if(Func.notNull(ignoreSecure) && ignoreSecure.permission()){
			return true;
		}
		// 读取权限注解，优先方法上，没有则读取类
		PreAuth preAuth = ClassUtil.getAnnotation(method, PreAuth.class);
		// 判断表达式
		String condition = preAuth.el();
		if (Func.isNotEmpty(preAuth.any())){
			condition = String.format(AuthConstant.HAS_ANY_PERMISSION_TEMPLATE, Func.join(preAuth.any(), StringPool.SINGLE_QUOTE+StringPool.COMMA+StringPool.SINGLE_QUOTE));
		}
		if (Func.isNotEmpty(preAuth.all())){
			condition = String.format(AuthConstant.HAS_ALL_PERMISSION_TEMPLATE, Func.join(preAuth.all(), StringPool.SINGLE_QUOTE+StringPool.COMMA+StringPool.SINGLE_QUOTE));
		}
		if (StringUtil.isNotBlank(condition)) {
			Expression expression = SPEL_PARSER.parseExpression(condition);
			// 方法参数值
			Object[] args = point.getArgs();
			StandardEvaluationContext context = getEvaluationContext(method, args);
			Boolean value = expression.getValue(context, Boolean.class);
			return value != null && value;
		}
		return false;
	}

	/**
	 * 获取方法上的参数
	 *
	 * @param method 方法
	 * @param args   变量
	 * @return {SimpleEvaluationContext}
	 */
	private StandardEvaluationContext getEvaluationContext(Method method, Object[] args) {
		// 初始化Sp el表达式上下文，并设置 AuthFun
		StandardEvaluationContext context = new StandardEvaluationContext(new AuthFun());
		// 设置表达式支持spring bean
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
			// 设置方法 参数名和值 为sp el变量
			context.setVariable(methodParam.getParameterName(), args[i]);
		}
		return context;
	}

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
