package top.kthirty.core.db.dict;

import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
/**
 * <p>
 * 数据字典装配
 * </p>
 *
 * @author KThirty
 * @since 2023/11/22
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(KthirtyDictProperties.class)
@AutoConfigureAfter(MybatisFlexAutoConfiguration.class)
@ConditionalOnClass({DictProvider.class})
public class DictConfiguration {

    /**
     * 有Redis
     */
    @Bean
    @Order(0)
    @ConditionalOnMissingBean(DictProvider.class)
    @ConditionalOnBean({RedisUtil.class,KthirtyDictProperties.class})
    public DictProvider dictProvider(RedisUtil redisUtil,KthirtyDictProperties kthirtyDictProperties) {
        return new DefaultDictProvider(redisUtil,kthirtyDictProperties);
    }

    /**
     * 无Redis
     */
    @Bean
    @Order(1)
    @ConditionalOnMissingBean(DictProvider.class)
    @ConditionalOnBean({KthirtyDictProperties.class})
    public DictProvider dictProvider(KthirtyDictProperties kthirtyDictProperties) {
        return new DefaultDictProvider(null,kthirtyDictProperties);
    }
}
