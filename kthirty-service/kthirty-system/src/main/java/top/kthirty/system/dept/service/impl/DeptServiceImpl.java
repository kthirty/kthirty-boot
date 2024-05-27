package top.kthirty.system.dept.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.system.dept.entity.Dept;
import top.kthirty.system.dept.mapper.DeptMapper;
import top.kthirty.system.dept.service.DeptService;

import java.util.List;

/**
 * 部门信息 服务层实现。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    @Override
    public boolean save(Dept entity) {
        TreePath.setCode(entity);
        return super.save(entity);
    }

    @Override
    public List<Tree<String>> tree() {
        return TreeUtil.build(list().stream()
                .map(it -> new TreeNode<String>()
                        .setId(it.getId())
                        .setWeight(it.getSort())
                        .setName(it.getName())
                        .setParentId(it.getParentId())
                        .setExtra(Kv.init().setAll(it.jsonAnyGetter()).setAll(BeanUtil.toMap(it))))
                .toList(),"0");
    }
}
