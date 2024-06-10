package top.kthirty.system.service;

import com.mybatisflex.core.query.QueryWrapper;
import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.system.entity.Menu;

import java.util.List;

/**
 * 菜单 服务层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface MenuService extends BaseService<Menu> {

    List<Menu> tree(QueryWrapper wrapper);
}
