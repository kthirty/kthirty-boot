package top.kthirty.flowable.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.flowable.common.engine.api.async.AsyncTaskExecutor;
import org.flowable.common.engine.impl.cfg.IdGenerator;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.spring.AutoDeploymentStrategy;
import org.flowable.engine.ProcessEngine;
import org.flowable.http.common.api.client.FlowableHttpClient;
import org.flowable.job.service.impl.asyncexecutor.AsyncExecutor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.*;
import org.flowable.spring.boot.app.FlowableAppProperties;
import org.flowable.spring.boot.eventregistry.FlowableEventRegistryProperties;
import org.flowable.spring.boot.idm.FlowableIdmProperties;
import org.flowable.spring.boot.process.FlowableProcessProperties;
import org.flowable.spring.boot.process.Process;
import org.flowable.spring.boot.process.ProcessAsync;
import org.flowable.spring.boot.process.ProcessAsyncHistory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.flowable.support.FlowableExpressionUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 替换默认ProcessEngineAutoConfiguration
 * 1. MybatisFlex事务与Flowable事务冲突整合（MybatisFlex重写了DataSource 和 PlatformTransactionManager）
 * 2. 基础配置
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Configuration
@AutoConfigureOrder(-1)
public class CustomeProcessEngineAutoConfiguration extends ProcessEngineAutoConfiguration {

    public CustomeProcessEngineAutoConfiguration(FlowableProperties flowableProperties, FlowableProcessProperties processProperties,
                                          FlowableAppProperties appProperties, FlowableIdmProperties idmProperties,
                                          FlowableEventRegistryProperties eventProperties, FlowableMailProperties mailProperties,
                                          FlowableHttpProperties httpProperties, FlowableAutoDeploymentProperties autoDeploymentProperties) {
        super(flowableProperties,processProperties,appProperties,idmProperties,eventProperties,mailProperties,httpProperties,autoDeploymentProperties);
    }
    @Bean
    @ConditionalOnMissingBean
    public FlowableGlobalListener flowableGlobalListener() {
        return new FlowableGlobalListener();
    }
    @Bean
    @ConditionalOnMissingBean
    public FlowableIdGenerator flowableIdGenerator() {
        return new FlowableIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager platformTransactionManager,
                                                                             ObjectProvider<ObjectMapper> objectMapperProvider,
                                                                             @Process ObjectProvider<IdGenerator> processIdGenerator,
                                                                             ObjectProvider<IdGenerator> globalIdGenerator,
                                                                             @ProcessAsync ObjectProvider<AsyncExecutor> asyncExecutorProvider,
                                                                             @Qualifier("applicationTaskExecutor") ObjectProvider<org.springframework.core.task.AsyncTaskExecutor> applicationTaskExecutorProvider,
                                                                             @ProcessAsyncHistory ObjectProvider<AsyncExecutor> asyncHistoryExecutorProvider,
                                                                             ObjectProvider<org.springframework.core.task.AsyncTaskExecutor> taskExecutor,
                                                                             @Process ObjectProvider<org.springframework.core.task.AsyncTaskExecutor> processTaskExecutor,
                                                                             @Qualifier("flowableAsyncTaskInvokerTaskExecutor") ObjectProvider<AsyncTaskExecutor> asyncTaskInvokerTaskExecutor,
                                                                             ObjectProvider<FlowableHttpClient> flowableHttpClient,
                                                                             ObjectProvider<AutoDeploymentStrategy<ProcessEngine>> processEngineAutoDeploymentStrategies,
                                                                             SqlSessionFactory sqlSessionFactory,
                                                                             FlowableGlobalListener flowableGlobalListener,
                                                                             FlowableIdGenerator flowableIdGenerator) throws IOException {
        SpringProcessEngineConfiguration processEngineConfiguration = super.springProcessEngineConfiguration(dataSource, platformTransactionManager, objectMapperProvider, processIdGenerator, globalIdGenerator, asyncExecutorProvider, applicationTaskExecutorProvider, asyncHistoryExecutorProvider, taskExecutor, processTaskExecutor, asyncTaskInvokerTaskExecutor, flowableHttpClient, processEngineAutoDeploymentStrategies);
        // 设置数据源
        processEngineConfiguration.setDataSource(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource());
        // 设置事务管理器
        processEngineConfiguration.setTransactionManager(platformTransactionManager);
        // 配置自定义 ID 生成器
        processEngineConfiguration.setIdGenerator(flowableIdGenerator);
        // 添加全局事件监听器
        processEngineConfiguration.setEventListeners(List.of(flowableGlobalListener));
        // 设置字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        return processEngineConfiguration;
    }

    @Bean
    public Filter flowableAuthFilter() {
        return (servletRequest, servletResponse, filterChain) -> {
            Authentication.setAuthenticatedUserId(SecureUtil.getUsername());
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }

    @Bean(name = "flowExp")
    public FlowableExpressionUtil flowableExpressionUtil() {
        return new FlowableExpressionUtil();
    }

}
