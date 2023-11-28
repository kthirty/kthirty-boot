package top.kthirty.core.secure.expand;

import org.springframework.boot.builder.SpringApplicationBuilder;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.launch.LauncherService;

/**
 * <p>
 * 禁用自定义鉴权拦截器
 * </p>
 *
 * @author KThirty
 * @since 2022/5/12
 */
public class DisableKthirtySecureInterceptorLauncherImpl implements LauncherService {
    @Override
    public void launcher(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo) {
        launchInfo.addProperties("kthirty.secure.interceptors-enabled",false);
    }
}
