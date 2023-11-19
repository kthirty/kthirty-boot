package top.kthirty.core.tool.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.tool.redis.RedisTemplateConfiguration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({ RedisTemplateConfiguration.class})
@ConditionalOnClass({RedissonClient.class})
@Slf4j
public class RedissonConfiguration {
    /**
     * Redisson 分布式锁自动装配
     * @param redissonClient RedissonClient
     * @return GlobalLockAspect
     */
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public GlobalLockAspect authAspect(RedissonClient redissonClient) {
        log.info("Redisson GlobalLockAspect init success");
        return new GlobalLockAspect(redissonClient);
    }
}
