package top.kthirty.core.db.base.service;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.core.db.base.mapper.BaseMapper;

/**
 * <p>
 * service实现类基类
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M,T> implements BaseService<T> {
}
