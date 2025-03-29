package top.kthirty.core.db.fill.handler;

import top.kthirty.core.db.fill.FillHandler;

import java.lang.reflect.Field;
import java.util.Date;
/**
 * <p>
 * 填充当前时间
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
public class CurrentTimeHandler implements FillHandler<Date> {
    @Override
    public Date getVal(Object object, Class<?> clazz, Field field, String... args) {
        return new Date();
    }
}
