package top.kthirty.core.db.launch;

import net.dreamlu.mica.auto.annotation.AutoService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.launch.LauncherService;

/**
 * <p>
 * 核心db环境
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
@AutoService(LauncherService.class)
public class CoreDbLaunchImpl implements LauncherService {
    @Override
    public void launcher(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo) {
        processDefaultEnv(launchInfo);
    }

    /**
     * 处理默认环境变量
     * @param launchInfo 部署信息
     */
    private void processDefaultEnv(KthirtyLaunchInfo launchInfo) {
        launchInfo.addProperties("spring.datasource.type","com.zaxxer.hikari.HikariDataSource");
        launchInfo.addProperties("spring.datasource.hikari.minimum-idle","5");
        launchInfo.addProperties("spring.datasource.hikari.idle-timeout","600000");
        launchInfo.addProperties("spring.datasource.hikari.maximum-pool-size","10");
        launchInfo.addProperties("spring.datasource.hikari.max-lifetime","120000");
        launchInfo.addProperties("spring.datasource.hikari.connection-timeout","30000");
        launchInfo.addProperties("spring.datasource.hikari.auto-commit",true);
        launchInfo.addProperties("mybatis-flex.global-config.print-banner",false);
        launchInfo.addProperties("mybatis-flex.configuration.log-impl","org.apache.ibatis.logging.nologging.NoLoggingImpl");
    }
}
