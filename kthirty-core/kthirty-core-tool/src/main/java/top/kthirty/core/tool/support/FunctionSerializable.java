package top.kthirty.core.tool.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <p>
 * 支持序列化的Function
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
@FunctionalInterface
public interface FunctionSerializable<T, R> extends Function<T, R>, Serializable {
}
