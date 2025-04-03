package top.kthirty.core.db.base.mp;


import top.kthirty.core.db.config.MyBatisFlexConfiguration;

/**
 * <p>
 * Mapper基类，方便后续添加方法
 * 必须实现此接口才能被识别为Mapper
 * </p>
 *
 * @see MyBatisFlexConfiguration
 * @author KThirty
 * @since 2023/11/19
 */
public interface BaseMapper<T> extends com.mybatisflex.core.BaseMapper<T> {
}
