package top.kthirty.core.tool.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration(proxyBeanMethods = false)
@Order
public class CacheConfiguration {
    /**
     * 默认缓存机制
     */
    @Bean(name = "cacheHandler")
    @ConditionalOnMissingBean(CacheHandler.class)
    public CacheHandler cacheHandler() {
        return new LocalCacheHandler();
    }
}
