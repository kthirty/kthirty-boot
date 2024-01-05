package top.kthirty.system.dept.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.system.dept.entity.Dept;
import top.kthirty.system.dept.mapper.DeptMapper;
import top.kthirty.system.dept.service.DeptService;
import org.springframework.stereotype.Service;

/**
 * 部门信息 服务层实现。
 *
 * @author Thinkpad
 * @since 2024-01-05
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}
