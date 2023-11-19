package top.kthirty.core.boot.constant;

/**
 * <p>
 * 系统基础配置
 * </p>
 *
 * @author Kthirty
 * @since 2023/11/19
 */
public class AppConstant {
    /**
     * 基础包名
     */
    public static String[] BASE_PACKAGES = new String[]{};

    public static String basePackages(){
        return String.join(",", BASE_PACKAGES);
    }
}
