package top.kthirty.system.role.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.role.entity.Role;
import top.kthirty.system.role.mapper.RoleMapper;
import top.kthirty.system.role.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色 服务层实现。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
