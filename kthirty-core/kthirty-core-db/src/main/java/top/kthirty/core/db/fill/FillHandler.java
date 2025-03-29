package top.kthirty.core.db.fill;

import top.kthirty.core.db.base.entity.BaseEntity;

import java.lang.reflect.Field;
/**
 * <p>
 * 自动填充处理器
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
public interface FillHandler<Type> {

    /**
     * 获取填充值
     * @param object 实例
     * @param clazz 类
     * @param field 字段
     * @param args 额外参数
     * @return 填充值
     */
    Type getVal(Object object, Class<?> clazz, Field field,String... args);
}
