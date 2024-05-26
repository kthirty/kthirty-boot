package top.kthirty.system.menu.service;

import cn.hutool.core.lang.tree.Tree;
import com.mybatisflex.core.query.QueryWrapper;
import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.menu.entity.Menu;

import java.util.List;

/**
 * 菜单 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface MenuService extends BaseService<Menu> {

    List<Tree<String>> tree(QueryWrapper wrapper);
}
