package top.kthirty.core.tool.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 枚举工具
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class EnumUtil extends cn.hutool.core.util.EnumUtil {

    /**
     * 匹配枚举
     * @param enumClass 枚举class
     * @param value 枚举值
     * @param <E> 枚举类型
     * @return 枚举本身（未匹配返回null）
     */
    public static <E extends Enum<E>> E match(Class<E> enumClass, Object value) {
        return likeValueOf(enumClass,value);
    }

    /**
     * 获取匹配到的枚举属性
     */
    @SuppressWarnings("unchecked")
    public static Object getField(Class<? extends Enum> enumClass, Object value,String field) {
        Enum match = match(enumClass, value);
        return ObjectUtil.isNull(match)?StringPool.EMPTY: ReflectUtil.getFieldValue(match, field);
    }

    /**
     * 获取枚举label
     */
    public static String getLabel(Class<? extends Enum> enumClass, Object value) {
        return StrUtil.toString(getField(enumClass,value,"label"));
    }


}
