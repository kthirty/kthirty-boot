package top.kthirty.core.tool.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.kthirty.core.tool.cache.CacheConfiguration;
import top.kthirty.core.tool.cache.CacheHandler;
import top.kthirty.core.tool.cache.RedisCacheHandler;

import java.time.Duration;
/**
 * @description Redis缓存管理
 * @author KThirty
 * @since 2025/4/14 11:00
 */
@Slf4j
@EnableCaching
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(CacheConfiguration.class)
@AutoConfigureAfter({RedisTemplateConfiguration.class})
@ConditionalOnBean(RedisTemplate.class)
public class RedisCacheConfiguration {

    @Bean
    @ConditionalOnMissingBean({CacheManager.class})
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<?> keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
        RedisSerializationContext.SerializationPair<?> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(keySerializer);
        RedisSerializationContext.SerializationPair<?> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer);
        @SuppressWarnings("unchecked")
        org.springframework.data.redis.cache.RedisCacheConfiguration redisCacheConfiguration = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeKeysWith((RedisSerializationContext.SerializationPair<String>) keySerializationPair)
                .serializeValuesWith(valueSerializationPair);

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(name = "redisUtil")
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUtil redisUtils() {
        return new RedisUtil();
    }

    @Bean(name = "cacheHandler")
    @ConditionalOnBean(RedisTemplate.class)
    @ConditionalOnMissingBean(CacheHandler.class)
    public CacheHandler cacheHandler() {
        return new RedisCacheHandler();
    }
}
