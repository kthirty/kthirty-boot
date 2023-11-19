package top.kthirty.core.db.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * <p>
 * 泛型工具类
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class GenericsUtil {
    /**
     * 获取类的泛型Class
     * @param clazz 当前类
     * @param index 第几个泛型0开始
     * @param <T> 泛型Class的类型
     * @return class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getActualTypeArgument(Class<?> clazz,int index) {
        try {
            Type type = clazz.getGenericSuperclass();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return (Class<T>) parameterizedType.getActualTypeArguments()[index];
        }catch (Throwable e){
            throw new RuntimeException("获取类泛型类失败",e);
        }
    }
}
