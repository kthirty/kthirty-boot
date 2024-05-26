package top.kthirty.system.menu.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.system.menu.entity.Menu;
import top.kthirty.system.menu.mapper.MenuMapper;
import top.kthirty.system.menu.service.MenuService;

import java.util.List;

/**
 * 菜单 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public boolean save(Menu entity) {
        TreePath.setCode(entity,"id");
        return super.save(entity);
    }

    @Override
    public List<Tree<String>> tree(QueryWrapper wrapper) {
        List<TreeNode<String>> nodes = list(wrapper).stream().map(it -> {
            TreeNode<String> node = new TreeNode<>();
            Kv extra = Kv.init().setAll(BeanUtil.toMap(it)).setAll(it.jsonAnyGetter());
            node.setWeight(Func.toInt(it.getSort(),0)).setExtra(extra);
            return node;
        }).toList();
        return TreeUtil.build(nodes, "0");
    }
}
