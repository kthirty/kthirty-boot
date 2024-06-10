package top.kthirty.system.service;

import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.Dept;

import java.util.List;

/**
 * 部门信息 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface DeptService extends BaseService<Dept> {

    List<Dept> tree();
}
