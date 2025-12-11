package top.kthirty.extra.report;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

/**
 * Report应用程序入口类
 * @author KThirty
 * @since 2025/7/22 17:15
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class ReportApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(ReportApplication.class, args);
    }
}
