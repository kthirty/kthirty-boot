package top.kthirty.flowable;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
public class FlowableApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(FlowableApplication.class,args);
    }
}
