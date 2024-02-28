package top.kthirty.core.web;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import top.kthirty.core.web.utils.INetUtil;

/**
 * 项目启动事件通知
 *
 * @author Kthirty
 */
@Slf4j
@Configuration
public class StartEventListener {

    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        String contextPath = environment.getProperty("server.servlet.context-path");
        int localPort = event.getWebServer().getPort();
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        System.out.printf("-----------[%s]启动完成----------\n", appName);
        System.out.printf("-----------当前端口:[%s]，环境:[%s]-----------\n", localPort, profile);
        System.out.printf("-----------Swagger Doc : [http://%s:%s%s/doc.html]-----------\n"
                , INetUtil.getHostIp()
                , localPort
                , StrUtil.isNotBlank(contextPath) ? contextPath : "");
    }
}
