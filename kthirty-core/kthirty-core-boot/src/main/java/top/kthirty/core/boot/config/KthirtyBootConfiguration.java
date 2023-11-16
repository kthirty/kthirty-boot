package top.kthirty.core.boot.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import top.kthirty.core.boot.props.KthirtyProperties;

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
public class KthirtyBootConfiguration {}