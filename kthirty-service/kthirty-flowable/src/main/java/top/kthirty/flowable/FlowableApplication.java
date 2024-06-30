package top.kthirty.flowable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@MapperScan(basePackages = "top.kthirty.**.mapper")
public class FlowableApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(FlowableApplication.class,args);
    }
}
