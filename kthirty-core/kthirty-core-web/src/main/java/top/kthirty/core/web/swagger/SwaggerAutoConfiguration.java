package top.kthirty.core.web.swagger;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
    public WebMvcConfigurer faviconWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
                        if (HttpMethod.GET.matches(request.getMethod()) && request.getRequestURI().equals("/favicon.ico")) {
                            response.setStatus(HttpStatus.NO_CONTENT.value()); // 设置状态码为204 No Content
                            return false;
                        }
                        return true;
                    }
                }).addPathPatterns("/**");
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI customOpenApi(SwaggerProperties swaggerProperties) {

        SecurityScheme scheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer");
        if(StrUtil.isNotBlank(swaggerProperties.getAuthorization().getTokenUrlList())){
            scheme.type(SecurityScheme.Type.OAUTH2);
            OAuthFlows oAuthFlows = new OAuthFlows();
            OAuthFlow oAuthFlow = new OAuthFlow();
            oAuthFlow.setTokenUrl(swaggerProperties.getAuthorization().getTokenUrlList());
            oAuthFlows.setPassword(oAuthFlow);
            scheme.flows(oAuthFlows);
        }else{
            scheme.type(SecurityScheme.Type.HTTP);
        }

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
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION, scheme));
    }

}
