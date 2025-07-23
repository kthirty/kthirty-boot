package top.kthirty.extra.report.config;

import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.secure.interceptor.SecureRegistry;
import top.kthirty.extra.report.config.filter.UReportAuthFilter;

/**
 * UReport权限配置类
 * @author KThirty
 * @since 2025/7/22 17:24
 */
@Configuration
@ConditionalOnBean(SecureRegistry.class)
@ConditionalOnProperty(value = "report.auth", havingValue = "true", matchIfMissing = true)
public class ReportAuthConfiguration {
    @Bean
    @ConditionalOnProperty(name = "kthirty.secure.enabled", matchIfMissing = true, havingValue = "true")
    public FilterRegistrationBean<Filter> uReportAuthFilter(SecureRegistry registry) {
        registry.excludePathPatterns("/ureport/**");
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UReportAuthFilter());
        registration.addUrlPatterns("/ureport/*");
        registration.setName("uReportAuthFilter");
        registration.setOrder(1);
        return registration;
    }
}
