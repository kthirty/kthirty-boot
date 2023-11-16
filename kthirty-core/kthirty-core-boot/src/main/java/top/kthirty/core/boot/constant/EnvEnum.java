package top.kthirty.core.boot.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 环境相关常量
 * @author Kthirty
 * @since Created in 2023/11/16
 */
@AllArgsConstructor
@Getter
public enum EnvEnum {
    /**
     * 开发环境
     */
    DEV_CODE("dev","开发环境"),
    /**
     * 生产环境
     */
    PROD_CODE("prod","生产环境"),
    /**
     * 测试环境
     */
    TEST_CODE("test","测试环境");
    private final String value;
    private final String desc;
}
