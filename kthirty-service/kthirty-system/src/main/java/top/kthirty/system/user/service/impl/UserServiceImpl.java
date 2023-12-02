package top.kthirty.system.user.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.user.entity.User;
import top.kthirty.system.user.mapper.UserMapper;
import top.kthirty.system.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息 服务层实现。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
