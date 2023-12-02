package top.kthirty.system.role.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.role.entity.Post;
import top.kthirty.system.role.mapper.PostMapper;
import top.kthirty.system.role.service.PostService;
import org.springframework.stereotype.Service;

/**
 * 岗位信息 服务层实现。
 *
 * @author KTHIRTY
 * @since 2023-12-02
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}
