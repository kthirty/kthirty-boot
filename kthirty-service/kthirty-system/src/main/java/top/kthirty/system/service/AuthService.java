package top.kthirty.system.service;

import cn.hutool.core.lang.tree.Tree;
import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.system.model.AuthParam;

import java.util.List;

public interface AuthService {
    TokenInfo token(AuthParam authParam);

    List<Tree<String>> menus();
}
