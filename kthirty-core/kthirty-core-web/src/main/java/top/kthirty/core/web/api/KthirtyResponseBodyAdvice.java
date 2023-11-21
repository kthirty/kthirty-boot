package top.kthirty.core.web.api;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.api.R;

import java.lang.annotation.Annotation;
/**
 * <p>
 * KthirtyResult 注解封装，响应自动封装
 * </p>
 *
 * @author KThirty
 * @since 2021/9/6
 */
@RestControllerAdvice
@Order(0)
public class KthirtyResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private static final String CONTENT_TYPE_NAME = "Content-type";
    private static final Class<? extends Annotation> ANNOTATION_TYPE = KthirtyResult.class;
    private static final Class<? extends Annotation> ANNOTATION_IGNORE_TYPE = KthirtyResultIgnore.class;

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        // 当方法或类上有标识且不包含忽略标识时才进行封装
        boolean hasKthirtyResult = AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), ANNOTATION_TYPE)
                || methodParameter.hasMethodAnnotation(ANNOTATION_TYPE);
        boolean notHasKthirtyResultIgnore = !AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), ANNOTATION_IGNORE_TYPE)
                && !methodParameter.hasMethodAnnotation(ANNOTATION_IGNORE_TYPE);
        return hasKthirtyResult && notHasKthirtyResultIgnore;
    }

    @Override
    public Object beforeBodyWrite(Object o,@NonNull  MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class<? extends HttpMessageConverter<?>> aClass, @NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse) {
        serverHttpResponse.getHeaders().add(CONTENT_TYPE_NAME,MediaType.APPLICATION_JSON_VALUE);
        // 防止重复包裹的问题出现
        if (o instanceof R) {
            return o;
        }
        if(o instanceof String){
            return Func.toJson(R.success(o));
        }
        return R.success(o);
    }
}
