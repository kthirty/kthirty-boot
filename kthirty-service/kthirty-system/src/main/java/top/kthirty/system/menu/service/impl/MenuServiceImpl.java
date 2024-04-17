package top.kthirty.system.menu.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import top.kthirty.core.db.support.TreePath;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.RuleCodeUtil;
import top.kthirty.system.menu.entity.Menu;
import top.kthirty.system.menu.mapper.MenuMapper;
import top.kthirty.system.menu.service.MenuService;
import org.springframework.stereotype.Service;

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
        TreePath.setCode(entity);
        return super.save(entity);
    }
}
