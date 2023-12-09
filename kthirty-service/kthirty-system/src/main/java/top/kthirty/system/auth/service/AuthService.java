package top.kthirty.system.auth.service;

import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.system.auth.model.AuthParam;
import top.kthirty.system.menu.entity.Menu;

import java.util.List;

public interface AuthService {
    TokenInfo token(AuthParam authParam);

    List<Menu> menus();
}
