package top.kthirty.core.boot.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import top.kthirty.core.boot.props.KthirtyProperties;
import top.kthirty.core.boot.secure.DefaultSysUserProvider;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUserProvider;

/**
 * 配置类
 * @author Kthirty
 * @since Created in 2023/11/16
 */
@Configuration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({
        KthirtyProperties.class
})
public class KthirtyBootConfiguration {

    @Bean
    @ConditionalOnClass(ApplicationContextAware.class)
    public SecureUtil secureUtil(){
        return new SecureUtil();
    }

    @Bean
    @ConditionalOnMissingBean(SysUserProvider.class)
    public SysUserProvider sysUserProvider(){
        return new DefaultSysUserProvider();
    }
}