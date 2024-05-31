package top.kthirty.system.service;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryWrapper;
import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.Post;

import java.util.List;

/**
 * 岗位信息 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface PostService extends BaseService<Post> {

    List<Tree<String>> tree(QueryWrapper wrapper);
}
