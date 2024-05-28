package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.entity.UserRoleRl;
import top.kthirty.system.mapper.UserRoleRlMapper;
import top.kthirty.system.service.UserRoleRlService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联表 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class UserRoleRlServiceImpl extends ServiceImpl<UserRoleRlMapper, UserRoleRl> implements UserRoleRlService {

}
