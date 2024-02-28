package top.kthirty.system.dept.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.RuleCodeUtil;
import top.kthirty.system.dept.entity.Dept;
import top.kthirty.system.dept.mapper.DeptMapper;
import top.kthirty.system.dept.service.DeptService;
import org.springframework.stereotype.Service;
import top.kthirty.system.menu.entity.Menu;

/**
 * 部门信息 服务层实现。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {
    @Override
    public boolean save(Dept entity) {
        TreePath.setCode(entity);
        return super.save(entity);
    }
}
