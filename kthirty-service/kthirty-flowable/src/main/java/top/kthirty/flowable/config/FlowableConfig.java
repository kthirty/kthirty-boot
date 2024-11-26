package top.kthirty.flowable.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * <p>
 * 流程全局配置
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
@Configuration
@RequiredArgsConstructor
public class FlowableConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final SpringProcessEngineConfiguration processEngineConfiguration;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setIdGenerator(new FlowableIdGenerator());
        processEngineConfiguration.getEventDispatcher().addEventListener(new FlowableGlobalListener());
    }
}
