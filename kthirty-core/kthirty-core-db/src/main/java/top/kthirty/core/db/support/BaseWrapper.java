package top.kthirty.core.db.support;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础类型转换器
 * <p>
 *     使用泛型工具类自动识别当前类型的泛型并使用BeanUtil.copy转换类型
 * </p>
 * @param <E> 实体
 * @param <V> VO
 * @author Kthirty
 * @since 2023/11/19
 */
public class BaseWrapper<E,V> extends BaseEntityWrapper<E,V>{
    @Override
    public V entityVO(E entity) {
        return entity == null ? null : BeanUtil.copyProperties(entity,GenericsUtil.getActualTypeArgument(getClass(),1));
    }
    public E entity(V entity) {
        return entity == null ? null : BeanUtil.copyProperties(entity,GenericsUtil.getActualTypeArgument(getClass(),0));
    }
    public List<E> listEntity(List<V> list) {
        return list.stream().map(this::entity).collect(Collectors.toList());
    }
}
