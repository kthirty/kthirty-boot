package top.kthirty.system.role.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.role.entity.Role;
import top.kthirty.system.role.mapper.RoleMapper;
import top.kthirty.system.role.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色 服务层实现。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
