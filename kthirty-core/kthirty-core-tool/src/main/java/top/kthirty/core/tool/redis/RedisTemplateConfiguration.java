package top.kthirty.core.tool.redis;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.kthirty.core.tool.utils.StringPool;

import java.time.Duration;
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
@EnableCaching
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedisAutoConfiguration.class, RedisSerializer.class})
@AutoConfigureBefore({RedisAutoConfiguration.class})
public class RedisTemplateConfiguration {

    /**
     * value 值 序列化
     *
     * @return RedisSerializer
     */
    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    @ConditionalOnBean(RedisSerializer.class)
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
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean({CacheManager.class})
    @ConditionalOnBean(RedisTemplate.class)
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer<?> keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer<?> valueSerializer = redisTemplate.getValueSerializer();
        RedisSerializationContext.SerializationPair<?> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(keySerializer);
        RedisSerializationContext.SerializationPair<?> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer);
        @SuppressWarnings("unchecked")
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
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

}
