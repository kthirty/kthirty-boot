package top.kthirty.core.web.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.kthirty.core.tool.Func;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * url与注解缓存
 * </p>
 *
 * @author KThirty
 * @since 2022/9/21
 */
public class RequestMappingHolder implements ApplicationContextAware {
    private static Map<RequestMappingInfo, HandlerMethod> handlerMethod = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        handlerMethod = handlerMapping.getHandlerMethods();
    }

    /**
     * 匹配
     * @param httpServletRequest 当前请求
     * @return HandlerMethod
     */
    public static HandlerMethod match(HttpServletRequest httpServletRequest) {
        for (Map.Entry<RequestMappingInfo, HandlerMethod> reqInfo : handlerMethod.entrySet()) {
            // 请求方式匹配
            List<String> matchMethod = reqInfo.getKey().getMethodsCondition().getMethods().stream()
                    .map(Enum::name)
                    .filter(method -> StrUtil.equalsIgnoreCase(method, httpServletRequest.getMethod()))
                    .collect(Collectors.toList());
            // url匹配
            List<String> matchingPatterns = reqInfo.getKey().getPatternsCondition().getMatchingPatterns(httpServletRequest.getRequestURI());
            // 匹配全成功
            if(Func.isNotEmpty(matchingPatterns) && Func.isNotEmpty(matchMethod)){
                return reqInfo.getValue();
            }
        }
        return null;
    }
    /**
     * 获取方法上或者类上的注解
     * @param httpServletRequest 请求信息
     * @return 注解实体
     */
    public static <A extends Annotation> A getAnnotation(HttpServletRequest httpServletRequest, Class<A> annotationType) {
        HandlerMethod match = match(httpServletRequest);
        if(match != null){
            A annotation = AnnotationUtil.getAnnotation(match.getMethod(), annotationType);
            if(annotation != null){
                return annotation;
            }
            return AnnotationUtil.getAnnotation(match.getMethod().getDeclaringClass(), annotationType);
        }else{
            return null;
        }
    }

    /**
     * 获取匹配成功的方法
     * @param httpServletRequest 请求
     * @return Method
     */
    public static Method getMethod(HttpServletRequest httpServletRequest){
        HandlerMethod match = match(httpServletRequest);
        return match != null ? match.getMethod() : null;
    }

}
