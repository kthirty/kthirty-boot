package top.kthirty.system.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.core.tool.utils.TreeUtil;
import top.kthirty.system.entity.Post;
import top.kthirty.system.mapper.PostMapper;
import top.kthirty.system.service.PostService;

import java.util.List;

/**
 * 岗位信息 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Override
    public List<Post> tree(QueryWrapper wrapper) {
        return TreeUtil.buildBean(this.list(wrapper));
    }

    @Override
    public boolean save(Post entity) {
        return super.save(entity);
    }
}
