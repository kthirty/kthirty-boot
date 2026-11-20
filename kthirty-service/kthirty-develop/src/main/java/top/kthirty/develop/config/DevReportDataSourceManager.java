package top.kthirty.develop.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PreDestroy;
import top.kthirty.core.tool.utils.SpringUtil;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报表数据源管理：按 dsKey 从 Properties 懒加载 HikariDataSource。
 * dsKey 为空或 primary 时使用 Spring 主数据源。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
public class DevReportDataSourceManager {

    public static final String PRIMARY_KEY = "primary";

    private final DevReportProperties properties;
    private final Map<String, HikariDataSource> cache = new ConcurrentHashMap<>();

    public DevReportDataSourceManager(DevReportProperties properties) {
        this.properties = properties;
    }

    /**
     * 已配置的数据源 Key（含 primary）
     */
    public Set<String> listKeys() {
        Map<String, DevReportProperties.DataSourceConfig> map = properties.getDatasources();
        if (map == null || map.isEmpty()) {
            return Set.of(PRIMARY_KEY);
        }
        java.util.LinkedHashSet<String> keys = new java.util.LinkedHashSet<>();
        keys.add(PRIMARY_KEY);
        keys.addAll(map.keySet());
        return Collections.unmodifiableSet(keys);
    }

    public DataSource getDataSource(String dsKey) {
        String key = StrUtil.blankToDefault(dsKey, PRIMARY_KEY);
        if (PRIMARY_KEY.equalsIgnoreCase(key) || StrUtil.isBlank(dsKey)) {
            DataSource primary = SpringUtil.getBeanSafe(DataSource.class);
            Assert.notNull(primary, "主数据源未配置");
            return primary;
        }
        return cache.computeIfAbsent(key, this::createDataSource);
    }

    private HikariDataSource createDataSource(String key) {
        DevReportProperties.DataSourceConfig config = properties.getDatasources() == null
                ? null : properties.getDatasources().get(key);
        Assert.notNull(config, "数据源[{}]未在 kthirty.develop.datasources 中配置", key);
        Assert.notBlank(config.getUrl(), "数据源[{}] url 未配置", key);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setPoolName("DevReport-" + key);
        hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize() == null ? 5 : config.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(config.getMinimumIdle() == null ? 1 : config.getMinimumIdle());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout() == null ? 30000L : config.getConnectionTimeout());
        hikariConfig.setAutoCommit(true);
        return new HikariDataSource(hikariConfig);
    }

    @PreDestroy
    public void destroy() {
        cache.values().forEach(ds -> {
            if (!ds.isClosed()) {
                ds.close();
            }
        });
        cache.clear();
    }
}
