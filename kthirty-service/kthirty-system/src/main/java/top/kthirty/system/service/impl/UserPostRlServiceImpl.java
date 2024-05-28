package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.entity.UserPostRl;
import top.kthirty.system.mapper.UserPostRlMapper;
import top.kthirty.system.service.UserPostRlService;
import org.springframework.stereotype.Service;

/**
 * 用户岗位关联表 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class UserPostRlServiceImpl extends ServiceImpl<UserPostRlMapper, UserPostRl> implements UserPostRlService {

}
