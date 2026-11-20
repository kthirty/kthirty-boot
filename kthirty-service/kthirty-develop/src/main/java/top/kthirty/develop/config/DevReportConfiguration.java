package top.kthirty.develop.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 报表开发模块配置
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Configuration
@EnableConfigurationProperties(DevReportProperties.class)
public class DevReportConfiguration {

    @Bean
    public DevReportDataSourceManager devReportDataSourceManager(DevReportProperties properties) {
        return new DevReportDataSourceManager(properties);
    }
}
