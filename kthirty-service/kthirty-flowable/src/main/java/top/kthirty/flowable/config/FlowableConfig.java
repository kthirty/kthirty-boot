package top.kthirty.flowable.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;


/**
 * <p>
 * 流程全局配置
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Configuration
@AutoConfigureOrder(-1)
public class FlowableConfig  {

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            SqlSessionFactory sqlSessionFactory,
            PlatformTransactionManager annotationDrivenTransactionManager) {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        // 设置数据源
        processEngineConfiguration.setDataSource(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource());
        // 设置事务管理器
        processEngineConfiguration.setTransactionManager(annotationDrivenTransactionManager);
        // 配置自定义 ID 生成器
        processEngineConfiguration.setIdGenerator(new FlowableIdGenerator());
        // 添加全局事件监听器
        processEngineConfiguration.setEventListeners(List.of(new FlowableGlobalListener()));

        return processEngineConfiguration;
    }
}
