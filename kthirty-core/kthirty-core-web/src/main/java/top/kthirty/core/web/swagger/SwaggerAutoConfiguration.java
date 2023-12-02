/*
package top.kthirty.core.web.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.boot.config.KthirtyBootConfiguration;

*/
/**
 * swagger配置
 *
 * @author Kthirty
 *//*

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SwaggerProperties.class)
@AllArgsConstructor
@AutoConfigureAfter(KthirtyBootConfiguration.class)
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI customOpenApi(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title("xitong ")
                        .summary("系统管理")
                        .contact(new Contact()
                                .name(swaggerProperties.getContact().getName())
                                .url(swaggerProperties.getContact().getUrl())
                                .email(swaggerProperties.getContact().getEmail()))
                        .title(swaggerProperties.getTitle())
                        .version(swaggerProperties.getVersion())
                        .description(swaggerProperties.getDescription())
                        .termsOfService(swaggerProperties.getTermsOfServiceUrl())
                        .license(new License()
                                .name(swaggerProperties.getLicense())
                                .url(swaggerProperties.getLicenseUrl()))
                        );
    }

}
*/
