package top.kthirty.core.boot.constant;

import cn.hutool.core.util.ArrayUtil;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;

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
    public static KthirtyLaunchInfo LAUNCH_INFO = new KthirtyLaunchInfo();

    public static String basePackages(){
        return String.join(",", BASE_PACKAGES);
    }
    public static void addBasePackages(String basePackage){
        AppConstant.BASE_PACKAGES = ArrayUtil.append(AppConstant.BASE_PACKAGES,basePackage);
    }
}
