package top.kthirty;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan
@EnableAutoTable
public class Starter {
    public static void main(String[] args) {
        KthirtyApplication.run(Starter.class,args);
    }
}
