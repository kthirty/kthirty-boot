package top.kthirty.core.web.error;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.Servlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.api.R;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.core.web.utils.WebUtil;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 全局异常处理，处理可预见的异常
 *
 * @author Kthirty
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@RestControllerAdvice
@AllArgsConstructor
public class KthirtyRestExceptionTranslator {
	private final List<ErrorHandler> errorHandlers;

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(MissingServletRequestParameterException e) {
		log.warn("缺少请求参数：{}", e.getMessage());
		String message = String.format("缺少必要的请求参数: %s", e.getParameterName());
		return R.fail(SystemResultCode.PARAM_ERROR, message);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(IllegalArgumentException e) {
		return R.fail(SystemResultCode.PARAM_ERROR, e.getMessage());
	}



	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(MethodArgumentTypeMismatchException e) {
		log.warn("请求参数格式错误：{}", e.getMessage());
		String message = String.format("请求参数格式错误: %s", e.getName());
		return R.fail(SystemResultCode.PARAM_ERROR, message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(MethodArgumentNotValidException e) {
		log.warn("参数验证失败：{}", e.getMessage());
		return handleError(e.getBindingResult());
	}

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(BindException e) {
		log.warn("参数绑定失败：{}", e.getMessage());
		return handleError(e.getBindingResult());
	}

	private R handleError(BindingResult result) {
		FieldError error = result.getFieldError();
		String field = Objects.requireNonNull(error).getField();
		if(result.getTarget() != null){
			try{
				Field fieldObj = ReflectUtil.getField(result.getTarget().getClass(), field);
				Schema annotation = AnnotationUtil.getAnnotation(fieldObj, Schema.class);
				if(annotation != null && Func.isNotBlank(annotation.description())){
					field = annotation.description();
				}
			}catch (Throwable ignore){}
		}
		String message = String.format("%s:%s", field, error.getDefaultMessage());
		return R.fail(SystemResultCode.PARAM_ERROR, message);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(ConstraintViolationException e) {
		log.warn("参数验证失败：{}", e.getMessage());
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		ConstraintViolation<?> violation = violations.iterator().next();
		String path = violation.getPropertyPath().toString();
		String message = String.format("%s:%s", path, violation.getMessage());
		return R.fail(SystemResultCode.PARAM_ERROR, message);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public R handleError(NoHandlerFoundException e) {
		log.error("404没找到请求:{}", e.getMessage());
		return R.fail(SystemResultCode.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleError(HttpMessageNotReadableException e) {
		return R.fail(SystemResultCode.MSG_NOT_READABLE, e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public R handleError(HttpRequestMethodNotSupportedException e) {
		String url = StringPool.EMPTY;
		HttpServletRequest request = WebUtil.getRequest();
		if(request != null){
			url = request.getRequestURI();
		}
		log.error("不支持当前请求方法:{},URL:{}", e.getMessage(),url);
		return R.fail(SystemResultCode.METHOD_NOT_SUPPORTED, e.getMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public R handleError(HttpMediaTypeNotSupportedException e) {
		log.error("不支持当前媒体类型:{}", e.getMessage());
		return R.fail(SystemResultCode.MEDIA_TYPE_NOT_SUPPORTED, e.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	public R handleError(Throwable e) {
		HttpServletResponse response = WebUtil.getResponse();
		if(Func.isNotEmpty(errorHandlers)){
			List<ErrorHandler> handlers = errorHandlers.stream().sorted(Comparator.comparing(ErrorHandler::getOrder)).toList();
			for (ErrorHandler handler : handlers) {
				if(handler.support(e)){
					return handler.handle(e, response);
				}
			}
		}
		log.error("未处理的异常",e);
		return R.fail(SystemResultCode.FAILURE);
	}

}
