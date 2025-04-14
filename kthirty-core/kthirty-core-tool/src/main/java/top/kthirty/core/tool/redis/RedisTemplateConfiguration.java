package top.kthirty.core.tool.redis;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.kthirty.core.tool.utils.StringPool;

import java.util.Locale;

/**
 * <p>
 * Redis 配置
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedisAutoConfiguration.class, RedisSerializer.class})
@AutoConfigureAfter({RedisAutoConfiguration.class})
@ConditionalOnBean(RedisTemplate.class)
public class RedisTemplateConfiguration {
    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer<Object> redisSerializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        String applicationName = SpringUtil.getProperty("spring.application.name").toLowerCase(Locale.ROOT);
        RedisKeySerializer redisKeySerializer = new RedisKeySerializer(applicationName + StringPool.COLON);
        // key 序列化
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        // value 序列化
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
