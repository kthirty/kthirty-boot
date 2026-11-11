package top.kthirty.system;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

@SpringBootApplication
@ConditionalOnProperty(name = "kthirty.cloud.enabled", havingValue = "true")
public class SystemApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(SystemApplication.class,args);
    }


}
