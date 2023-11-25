package top.kthirty.core.db.dict;

import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.tool.dict.DictProvider;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.tool.utils.SpringUtil;

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
     * 字典解析器
     */
    @Bean
    @ConditionalOnMissingBean(DictProvider.class)
    public DictProvider dictProvider(KthirtyDictProperties kthirtyDictProperties) {
        return new DefaultDictProvider(SpringUtil.getBeanSafe(RedisUtil.class),kthirtyDictProperties);
    }

}
