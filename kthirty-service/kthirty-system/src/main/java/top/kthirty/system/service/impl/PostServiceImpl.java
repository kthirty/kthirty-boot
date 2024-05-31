package top.kthirty.system.service.impl;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.utils.TreeUtil;
import top.kthirty.system.mapper.PostMapper;
import top.kthirty.system.entity.Post;
import top.kthirty.system.service.PostService;
import org.springframework.stereotype.Service;

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
    public List<Tree<String>> tree(QueryWrapper wrapper) {
        return TreeUtil.forest(this.list(wrapper));
    }

    @Override
    public boolean save(Post entity) {
        TreePath.setCode(entity);
        return super.save(entity);
    }
}
