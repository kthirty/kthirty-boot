package top.kthirty.extra.report.config;

import com.bstek.ureport.definition.datasource.BuildinDatasource;
import com.bstek.ureport.provider.report.ReportProvider;
import com.bstek.ureport.provider.report.file.FileReportProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.kthirty.extra.report.config.provider.DbReportProvider;
import top.kthirty.extra.report.config.provider.SpringDataSourceProvider;

import javax.sql.DataSource;

/**
 * UReport 配置类
 * @author KThirty
 * @since 2025/7/22 17:18
 */
@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@ConditionalOnBean(DataSource.class)
public class ReportDbConfiguration  {


    @Bean
    @Primary
    public ReportProvider reportProvider(FileReportProvider fileReportProvider){
        fileReportProvider.setDisabled(true);
        return new DbReportProvider();
    }
    @Bean
    @Primary
    public BuildinDatasource datasourceProvider(){
        return new SpringDataSourceProvider();
    }

    @Bean
    public ReportController reportController(@Autowired DbReportProvider dbReportProvider){
        return new ReportController(dbReportProvider);
    }
}
