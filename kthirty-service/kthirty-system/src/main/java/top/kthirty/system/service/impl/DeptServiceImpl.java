package top.kthirty.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.core.tool.utils.TreeUtil;
import top.kthirty.system.entity.Dept;
import top.kthirty.system.mapper.DeptMapper;
import top.kthirty.system.service.DeptService;

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
    public List<Dept> tree() {
        return TreeUtil.buildBean(list());
    }
}
