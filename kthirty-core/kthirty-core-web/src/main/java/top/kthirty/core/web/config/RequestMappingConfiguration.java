package top.kthirty.core.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.web.thread.RequestVariableCleanListener;
import top.kthirty.core.web.utils.RequestMappingHolder;
/**
 * <p>
 * 请求映射缓存
 * </p>
 *
 * @author KThirty
 * @since 2022/9/23
 */
@Configuration(proxyBeanMethods = false)
public class RequestMappingConfiguration {
    @Bean
    @ConditionalOnMissingBean(RequestMappingHolder.class)
    public RequestMappingHolder requestMappingHolder(){
        return new RequestMappingHolder();
    }


    @Bean
    public RequestVariableCleanListener requestVariableCleanListener(){
        return new RequestVariableCleanListener();
    }
}
