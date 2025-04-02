package top.kthirty.core.db.config;

import com.mybatisflex.spring.boot.MybatisFlexProperties;
import com.tangzc.mybatisflex.autotable.MybatisFlexAutoTableAutoConfig;
import org.dromara.autotable.core.AutoTableClassScanner;
import org.dromara.autotable.core.AutoTableMetadataAdapter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import top.kthirty.core.db.auto.KThirtyCustomAutoTableMetadataAdapter;

/**
 * <p>
 * 自动建表配置
 * </p>
 *
 * @author KThirty
 * @since 2025/3/30
 */
@Configuration
@AutoConfigureAfter(MybatisFlexAutoTableAutoConfig.class)
public class AutoTableConfiguration {
    /**
     * 覆盖原始配置，去除Table自动扫描
     */
    @Bean
    @Primary
    public AutoTableClassScanner customAutoTableClassScanner() {
        return new AutoTableClassScanner(){};
    }

    /**
     * 覆盖原始Bean，增强{@link top.kthirty.core.db.auto.Column}注解
     */
    @Bean
    @Primary
    @ConditionalOnBean(MybatisFlexProperties.class)
    public AutoTableMetadataAdapter customAutoTableMetadataAdapter(MybatisFlexProperties mybatisFlexProperties) {
        return new KThirtyCustomAutoTableMetadataAdapter(mybatisFlexProperties);
    }
}
