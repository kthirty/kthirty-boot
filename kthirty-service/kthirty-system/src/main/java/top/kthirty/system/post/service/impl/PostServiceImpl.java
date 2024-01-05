package top.kthirty.system.post.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.post.entity.Post;
import top.kthirty.system.post.mapper.PostMapper;
import top.kthirty.system.post.service.PostService;
import org.springframework.stereotype.Service;

/**
 * 岗位信息 服务层实现。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}
