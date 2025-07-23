package top.kthirty.extra.report.config;

import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.provider.image.ImageProvider;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kthirty.extra.report.config.provider.CustomImageProvider;

/**
 * UReport 配置类
 * @author KThirty
 * @since 2025/7/22 17:18
 */
@Configuration
@ImportResource(locations = "classpath:ureport-console-context.xml")
@PropertySource("classpath:ureport.properties")
public class ReportConfiguration implements WebMvcConfigurer {
    @Bean
    public ServletRegistrationBean<UReportServlet> servletRegistrationBean() {
        return new ServletRegistrationBean<>(new UReportServlet(), "/ureport/*");
    }

    @Bean
    public ImageProvider imageProvider(){
        return new CustomImageProvider();
    }
}
