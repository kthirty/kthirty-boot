package top.kthirty;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.tool.cache.Cache;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class Starter {
    public static void main(String[] args) {
        KthirtyApplication.run(Starter.class,args);
//        Cache.set("test","123");
        System.out.println(Cache.get("test")+"");
    }
}
