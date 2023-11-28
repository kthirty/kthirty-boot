package top.kthirty.core.web.swagger;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.boot.props.KthirtyProperties;

/**
 * swagger配置
 *
 * @author Kthirty
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SwaggerProperties.class)
@AllArgsConstructor
@ConditionalOnBean(KthirtyProperties.class)
public class SwaggerAutoConfiguration {
    private final KthirtyProperties kthirtyProperties;

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    @ConditionalOnBean(SwaggerProperties.class)
    public OpenAPI customOpenApi(SwaggerProperties swaggerProperties) {
        return new OpenAPI()
                .info(new Info()
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
