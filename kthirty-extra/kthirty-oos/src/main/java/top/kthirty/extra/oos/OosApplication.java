package top.kthirty.extra.oos;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

/**
 * 对象存储模块入口
 *
 * @author KThirty
 */
@SpringBootApplication
@ConditionalOnProperty(name = "kthirty.cloud.enabled", havingValue = "true")
public class OosApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(OosApplication.class, args);
    }
}
