package top.kthirty.extra.oos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.kthirty.extra.oos.enums.OosStorageType;

import java.util.ArrayList;
import java.util.List;

/**
 * 对象存储配置
 *
 * @author KThirty
 */
@Data
@ConfigurationProperties(prefix = "kthirty.oos")
public class OosProperties {

    /**
     * 是否启用对象存储模块
     */
    private boolean enabled = true;

    /**
     * 存储类型：local / minio / seaweedfs
     */
    private OosStorageType storageType = OosStorageType.LOCAL;

    /**
     * 下载 URL 前缀（返回给前端的相对路径）
     */
    private String urlPrefix = "/oos/file/download/";

    /**
     * 单文件最大字节数，默认 50MB
     */
    private long maxFileSize = 52_428_800L;

    /**
     * 允许的文件扩展名（小写，不含点），为空则不限制
     */
    private List<String> allowedExtensions = new ArrayList<>();

    private LocalConfig local = new LocalConfig();

    private MinioConfig minio = new MinioConfig();

    private SeaweedFsConfig seaweedfs = new SeaweedFsConfig();

    @Data
    public static class LocalConfig {
        /**
         * 本地存储根目录
         */
        private String basePath = "./data/oos";
    }

    @Data
    public static class MinioConfig {
        private String endpoint = "http://127.0.0.1:9000";
        private String accessKey = "minioadmin";
        private String secretKey = "minioadmin";
        private String bucket = "kthirty";
        /**
         * 是否自动创建 bucket
         */
        private boolean autoCreateBucket = true;
    }

    @Data
    public static class SeaweedFsConfig {
        /**
         * SeaweedFS Filer 地址
         */
        private String filerUrl = "http://127.0.0.1:8888";
        /**
         * 存储集合/目录前缀
         */
        private String collection = "kthirty";
    }
}
