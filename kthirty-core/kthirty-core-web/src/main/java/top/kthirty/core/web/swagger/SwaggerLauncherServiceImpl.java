package top.kthirty.core.web.swagger;

import net.dreamlu.mica.auto.annotation.AutoService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.kthirty.core.boot.constant.EnvEnum;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.launch.LauncherService;

/**
 * 初始化Swagger配置
 *
 * @author Kthirty
 */
@AutoService(LauncherService.class)
public class SwaggerLauncherServiceImpl implements LauncherService {

	@Override
	public void launcher(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo) {
		if(EnvEnum.PROD_CODE.getValue().equals(launchInfo.getEnv())){
			launchInfo.addProperties("knife4j.production", "true");
		}
		launchInfo.addProperties("knife4j.enable", "true");
		launchInfo.addProperties("knife4j.setting.enable-home-custom", "true");
		launchInfo.addProperties("knife4j.setting.home-custom-path", "classpath:swagger-md/home.md");

		// 隐藏models
		launchInfo.addProperties("knife4j.setting.enableSwaggerModels", "false");

		launchInfo.addProperties("knife4j.setting.enableFooter", "false");
		launchInfo.addProperties("knife4j.setting.enableFooterCustom", "true");
		launchInfo.addProperties("knife4j.setting.footerCustomContent", "2021 - Now By KTHIRTY");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
