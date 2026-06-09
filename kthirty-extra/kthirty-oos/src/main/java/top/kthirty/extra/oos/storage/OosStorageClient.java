package top.kthirty.extra.oos.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.kthirty.extra.oos.enums.OosStorageType;

import java.io.InputStream;

/**
 * 存储客户端接口
 *
 * @author KThirty
 */
public interface OosStorageClient {

    OosStorageType getType();

    StorageObject upload(UploadRequest request);

    StorageObject download(String objectKey);

    void delete(String objectKey);

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UploadRequest {
        private InputStream inputStream;
        private String objectKey;
        private String contentType;
        private long size;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class StorageObject {
        private String objectKey;
        private String bucket;
        private InputStream inputStream;
        private long size;
        private String contentType;
    }
}
