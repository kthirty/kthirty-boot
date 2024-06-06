package top.kthirty.core.db.permission;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DataPermissionAspect dataPermissionAspect(){
        return new DataPermissionAspect();
    }
}
