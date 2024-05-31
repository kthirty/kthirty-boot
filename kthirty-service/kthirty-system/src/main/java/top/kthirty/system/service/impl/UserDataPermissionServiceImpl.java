package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.entity.UserDataPermission;
import top.kthirty.system.mapper.UserDataPermissionMapper;
import top.kthirty.system.service.UserDataPermissionService;
import org.springframework.stereotype.Service;

/**
 *  服务层实现。
 *
 * @author Thinkpad
 * @since 2024-05-31
 */
@Service
public class UserDataPermissionServiceImpl extends ServiceImpl<UserDataPermissionMapper, UserDataPermission> implements UserDataPermissionService {

}
