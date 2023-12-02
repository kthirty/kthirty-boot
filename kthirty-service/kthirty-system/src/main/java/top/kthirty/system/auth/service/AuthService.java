package top.kthirty.system.auth.service;

import top.kthirty.core.secure.token.TokenInfo;
import top.kthirty.system.auth.model.AuthParam;

public interface AuthService {
    TokenInfo token(AuthParam authParam);
}
