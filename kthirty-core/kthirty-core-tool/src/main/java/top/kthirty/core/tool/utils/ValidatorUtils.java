package top.kthirty.core.tool.utils;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * hibernate Validator 手动调用
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class ValidatorUtils {
    private static final Validator VALIDATOR_FAST = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    private static final Validator VALIDATOR_ALL = Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     * @param domain 实体
     * @param <T> 实体类型
     */
    public static <T> void validateFast(T domain) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR_FAST.validate(domain);
        if(!validateResult.isEmpty()) {
            throw new IllegalArgumentException(validateResult.iterator().next().getMessage());
        }
    }

    /**
     * 校验所有字段并返回不合法字段
     * @param domain 实体类
     * @param <T> 实体类型
     */
    public static <T> void validateAll(T domain) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR_ALL.validate(domain);
        if(!validateResult.isEmpty()) {
            throw new IllegalArgumentException(validateResult.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(StringPool.COMMA)));
        }
    }
}
