package top.kthirty.extra.report.config.provider;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
/**
 * 数据源自动
 * @author KThirty
 * @since 2025/7/23 11:07
 */
public class SpringDataSourceProvider implements BuildinDatasource {
    @Autowired
    private DataSource dataSource;

    @Override
    public String name() {
        return "内置数据源";
    }

    @SneakyThrows
    @Override
    public Connection getConnection() {
        return dataSource.getConnection();
    }
}
