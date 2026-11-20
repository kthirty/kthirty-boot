package top.kthirty.develop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 报表多数据源配置。
 * <pre>
 * kthirty:
 *   develop:
 *     datasources:
 *       biz:
 *         url: jdbc:mysql://localhost:3306/biz
 *         username: root
 *         password: xxx
 *         driver-class-name: com.mysql.cj.jdbc.Driver
 * </pre>
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@Data
@ConfigurationProperties(prefix = "kthirty.develop")
public class DevReportProperties {

    /**
     * 额外数据源，key 对应 DevReport.dsKey
     */
    private Map<String, DataSourceConfig> datasources = new LinkedHashMap<>();

    @Data
    public static class DataSourceConfig {
        private String url;
        private String username;
        private String password;
        private String driverClassName = "com.mysql.cj.jdbc.Driver";
        private Integer maximumPoolSize = 5;
        private Integer minimumIdle = 1;
        private Long connectionTimeout = 30000L;
    }
}
