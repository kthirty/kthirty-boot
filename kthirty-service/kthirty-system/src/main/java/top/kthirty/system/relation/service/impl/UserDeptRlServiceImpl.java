package top.kthirty.system.relation.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.relation.entity.UserDeptRl;
import top.kthirty.system.relation.mapper.UserDeptRlMapper;
import top.kthirty.system.relation.service.UserDeptRlService;
import org.springframework.stereotype.Service;

/**
 * 用户部门关联表 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class UserDeptRlServiceImpl extends ServiceImpl<UserDeptRlMapper, UserDeptRl> implements UserDeptRlService {

}
