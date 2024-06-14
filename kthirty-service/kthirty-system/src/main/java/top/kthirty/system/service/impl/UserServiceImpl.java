package top.kthirty.system.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.core.db.base.service.BaseServiceImpl;
import top.kthirty.system.entity.User;
import top.kthirty.system.mapper.UserMapper;
import top.kthirty.system.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Override
    public <R> Page<R> pageAs(Page<R> page, QueryWrapper query, Class<R> asType) {
        return getMapper().paginateWithRelationsAs(page, query, asType);
    }
}
