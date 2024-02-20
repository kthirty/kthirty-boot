package top.kthirty.core.web.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.*;
import lombok.AllArgsConstructor;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import top.kthirty.core.boot.config.KthirtyBootConfiguration;


/**
 * swagger配置
 *
 * @author Kthirty
 */

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SwaggerProperties.class)
@AllArgsConstructor
@AutoConfigureAfter(KthirtyBootConfiguration.class)
public class SwaggerAutoConfiguration {

    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            // 全局添加鉴权参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((s, pathItem) -> {
                    pathItem.readOperations().forEach(operation -> {
                        operation.addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
                    });
                });
            }

        };
    }

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI customOpenApi(SwaggerProperties swaggerProperties) {
        OAuthFlows oAuthFlows = new OAuthFlows();
        OAuthFlow oAuthFlow = new OAuthFlow();
        oAuthFlow.setTokenUrl(swaggerProperties.getAuthorization().getTokenUrlList());
        oAuthFlows.setPassword(oAuthFlow);


        return new OpenAPI()
                .info(new Info()
                        .title(swaggerProperties.getTitle())
                        .summary(swaggerProperties.getDescription())
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
                ).addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
                                .flows(oAuthFlows)
                                .name(HttpHeaders.AUTHORIZATION)
                                .in(SecurityScheme.In.HEADER)
                                .type(SecurityScheme.Type.OAUTH2)
                                .scheme("bearer")));
    }

}
