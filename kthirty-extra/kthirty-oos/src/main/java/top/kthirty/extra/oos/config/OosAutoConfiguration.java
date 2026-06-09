package top.kthirty.extra.oos.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.extra.oos.enums.OosStorageType;
import top.kthirty.extra.oos.storage.LocalStorageClient;
import top.kthirty.extra.oos.storage.MinioStorageClient;
import top.kthirty.extra.oos.storage.OosStorageClient;
import top.kthirty.extra.oos.storage.SeaweedFsStorageClient;

/**
 * 对象存储自动配置
 *
 * @author KThirty
 */
@Configuration
@EnableConfigurationProperties(OosProperties.class)
@ConditionalOnProperty(prefix = "kthirty.oos", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OosAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OosStorageClient oosStorageClient(OosProperties properties) {
        OosStorageType storageType = properties.getStorageType();
        return switch (storageType) {
            case LOCAL -> new LocalStorageClient(properties);
            case MINIO -> new MinioStorageClient(properties);
            case SEAWEEDFS -> new SeaweedFsStorageClient(properties);
        };
    }
}
