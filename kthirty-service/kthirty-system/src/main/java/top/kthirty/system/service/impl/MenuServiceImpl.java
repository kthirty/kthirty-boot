package top.kthirty.system.service.impl;

import cn.hutool.log.StaticLog;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.core.tool.utils.TreeUtil;
import top.kthirty.system.entity.Menu;
import top.kthirty.system.mapper.MenuMapper;
import top.kthirty.system.service.MenuService;

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
    public List<Menu> tree(QueryWrapper wrapper) {
        List<Menu> menus = TreeUtil.buildBean(list(wrapper));
        StaticLog.info("menus {}", JsonUtil.toJson(menus));
        return menus;
    }
}
