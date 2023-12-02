package top.kthirty.core.secure.config;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.kthirty.core.boot.secure.SysUserProvider;
import top.kthirty.core.secure.auth.AuthConstant;
import top.kthirty.core.secure.exception.NotLoginException;
import top.kthirty.core.secure.exception.SecureException;
import top.kthirty.core.secure.interceptor.SecureInterceptor;
import top.kthirty.core.secure.interceptor.SecureRegistry;
import top.kthirty.core.secure.token.JwtCacheTokenProvider;
import top.kthirty.core.secure.token.JwtRedisTokenProvider;
import top.kthirty.core.secure.token.TokenProvider;
import top.kthirty.core.tool.api.R;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.core.web.error.ErrorHandler;
import top.kthirty.core.web.utils.WebUtil;

import java.util.Objects;


/**
 * 安全配置类
 *
 * @author Kthirty
 */
@Configuration
@EnableConfigurationProperties({KthirtySecureProperties.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecureConfiguration {

    @Bean
    @ConditionalOnMissingBean(SecureRegistry.class)
    public SecureRegistry secureRegistry() {
        return new SecureRegistry();
    }

    @ConditionalOnProperty(name = "kthirty.secure.enabled", matchIfMissing = true, havingValue = "true")
    @Bean
    @ConditionalOnBean(SecureRegistry.class)
    public WebMvcConfigurer webMvcConfigurer(KthirtySecureProperties secureProperties, SecureRegistry secureRegistry) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@NonNull InterceptorRegistry registry) {
                if (secureRegistry.isEnabled()) {
                    registry.addInterceptor(new SecureInterceptor())
                            .excludePathPatterns(secureRegistry.getExcludePatterns())
                            .excludePathPatterns(secureRegistry.getDefaultExcludePatterns())
                            .excludePathPatterns(secureProperties.getSkipUrl());
                }
            }
        };
    }

    /**
     * 有Redis时
     */
    @Bean
    @ConditionalOnClass(RedisUtil.class)
    @ConditionalOnBean({RedisUtil.class})
    @ConditionalOnMissingBean(TokenProvider.class)
    @Order(1)
    public TokenProvider jwtRedisTokenProvider(RedisUtil redisUtil,KthirtySecureProperties kthirtySecureProperties){
        return new JwtRedisTokenProvider(redisUtil,kthirtySecureProperties);
    }
    /**
     * 无Redis时，使用本地缓存
     */
    @Bean
    @ConditionalOnMissingBean(TokenProvider.class)
    @Order(1)
    public TokenProvider jwtCacheTokenProvider(KthirtySecureProperties kthirtySecureProperties){
        return new JwtCacheTokenProvider(kthirtySecureProperties);
    }

    /**
     * 用户获取器
     * @param provider token获取器
     */
    @Bean
    @Order(0)
    @ConditionalOnBean(TokenProvider.class)
    public SysUserProvider sysUserProvider(TokenProvider provider){
        return () -> {
            HttpServletRequest request = WebUtil.getRequest();
            if(Objects.isNull(request)){
                return null;
            }
            // 从请求头获取Token
            String header = request.getHeader(AuthConstant.REQUEST_AUTHORIZATION_HEADER);
            String parameter = request.getParameter(AuthConstant.REQUEST_AUTHORIZATION_HEADER);
            String token = StrUtil.blankToDefault(header,parameter);
            token = StrUtil.removePrefix(token,"Bearer ");
            return provider.getCurrentUser(token);
        };
    }


    /**
     * 未登录异常处理
     * @return bean
     */
    @Bean
    @ConditionalOnClass(ErrorHandler.class)
    public ErrorHandler notLoginHandler(){
        return new ErrorHandler() {
            @Override
            public boolean support(Throwable e) {
                return e instanceof NotLoginException;
            }
            @Override
            public R handle(Throwable e, HttpServletResponse response) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return R.fail(SystemResultCode.NOT_LOGIN);
            }
        };
    }

    /**
     * 未登录异常处理
     * @return bean
     */
    @Bean
    @ConditionalOnClass(ErrorHandler.class)
    public ErrorHandler secureHandler(){
        return new ErrorHandler() {
            @Override
            public boolean support(Throwable e) {
                return e instanceof SecureException;
            }
            @Override
            public R handle(Throwable e, HttpServletResponse response) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return R.fail(SystemResultCode.UN_AUTHORIZED);
            }
        };
    }

}
