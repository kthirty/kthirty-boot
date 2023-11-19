package top.kthirty.core.tool.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;
import top.kthirty.core.tool.support.FunctionSerializable;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * lambda 工具类
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class LambdaUtil {
    private static final Map<Class<?>, SerializedLambda> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();
    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String SET_PREFIX = "set";

    @SneakyThrows
    public static <T, U> Field getField(FunctionSerializable<T, U> fn) {
        String fieldName = getFieldName(fn);
        Assert.notBlank(fieldName, "未获取到字段名，此方法仅支持get、set、is开头的方法");
        // 获取字段
        SerializedLambda lambda = getSerializedLambda(fn);
        String className = StringUtil.replace(lambda.getImplClass(), StringPool.SLASH, StringPool.DOT);
        Class<?> aClass = ClassUtil.loadClass(className);
        Field field = ReflectUtil.getField(aClass, fieldName);
        Assert.notNull(field, "获取字段失败");
        return field;
    }

    @SneakyThrows
    public static <T, U> String getFieldName(FunctionSerializable<T, U> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String fieldName = StringPool.EMPTY;
        // 获取字段名
        if (StringUtil.startsWithIgnoreCase(methodName, GET_PREFIX)) {
            fieldName = StringUtil.lowerFirst(methodName.replace(GET_PREFIX, ""));
        }
        if (StringUtil.startsWithIgnoreCase(methodName, IS_PREFIX)) {
            fieldName = StringUtil.lowerFirst(methodName.replace(IS_PREFIX, ""));
        }
        if (StringUtil.startsWithIgnoreCase(methodName, SET_PREFIX)) {
            fieldName = StringUtil.lowerFirst(methodName.replace(SET_PREFIX, ""));
        }
        return fieldName;
    }

    @SneakyThrows
    public static <T, U, A extends Annotation> A getFieldAnnotation(FunctionSerializable<T, U> fn, Class<A> clazz) {
        Field field = getField(fn);
        return field.getAnnotation(clazz);
    }

    public static SerializedLambda getSerializedLambda(Serializable fn) {
        SerializedLambda lambda = CLASS_LAMBDA_CACHE.get(fn.getClass());
        if (lambda == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMBDA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return lambda;
    }
}
