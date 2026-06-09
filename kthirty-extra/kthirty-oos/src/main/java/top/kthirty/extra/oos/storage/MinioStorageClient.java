package top.kthirty.extra.oos.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.extra.oos.config.OosProperties;
import top.kthirty.extra.oos.enums.OosStorageType;

/**
 * MinIO 对象存储
 *
 * @author KThirty
 */
@Slf4j
public class MinioStorageClient implements OosStorageClient {

    private final OosProperties properties;
    private final MinioClient minioClient;
    private final String bucket;

    public MinioStorageClient(OosProperties properties) {
        this.properties = properties;
        OosProperties.MinioConfig config = properties.getMinio();
        this.bucket = config.getBucket();
        this.minioClient = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();
        initBucket();
    }

    private void initBucket() {
        if (!properties.getMinio().isAutoCreateBucket()) {
            return;
        }
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize MinIO bucket: " + bucket, e);
        }
    }

    @Override
    public OosStorageType getType() {
        return OosStorageType.MINIO;
    }

    @Override
    public StorageObject upload(UploadRequest request) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(request.getObjectKey())
                    .stream(request.getInputStream(), request.getSize(), -1)
                    .contentType(request.getContentType())
                    .build());
            return StorageObject.builder()
                    .objectKey(request.getObjectKey())
                    .bucket(bucket)
                    .size(request.getSize())
                    .contentType(request.getContentType())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException("MinIO upload failed", e);
        }
    }

    @Override
    public StorageObject download(String objectKey) {
        try {
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build());
            return StorageObject.builder()
                    .objectKey(objectKey)
                    .bucket(bucket)
                    .inputStream(minioClient.getObject(GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .build()))
                    .size(stat.size())
                    .contentType(stat.contentType())
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("MinIO file not found: " + objectKey, e);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .build());
        } catch (Exception e) {
            throw new IllegalStateException("MinIO delete failed", e);
        }
    }
}
