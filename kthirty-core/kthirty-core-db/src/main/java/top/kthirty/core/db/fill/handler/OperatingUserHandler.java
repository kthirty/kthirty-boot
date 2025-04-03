package top.kthirty.core.db.fill.handler;

import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.db.fill.FillHandler;

import java.lang.reflect.Field;
/**
 * <p>
 * 当前操作人
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
public class OperatingUserHandler implements FillHandler<String> {
    @Override
    public String getVal(Object object, Class<?> clazz, Field field,String[] args) {
        return SecureUtil.getUsername();
    }
}
