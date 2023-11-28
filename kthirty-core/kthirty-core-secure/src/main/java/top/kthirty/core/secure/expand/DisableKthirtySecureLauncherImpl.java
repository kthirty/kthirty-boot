package top.kthirty.core.secure.expand;

import org.springframework.boot.builder.SpringApplicationBuilder;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.launch.LauncherService;

/**
 * <p>
 * 禁用自定义鉴权
 * </p>
 *
 * @author KThirty
 * @since 2022/5/12
 */
public class DisableKthirtySecureLauncherImpl implements LauncherService {
    public static final String CLASS_NAME = "top.kthirty.core.secure.expand.DisableKthirtySecureLauncherImpl";
    @Override
    public void launcher(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo) {
        launchInfo.addProperties("kthirty.secure.enabled",false);
    }
}
