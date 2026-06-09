package top.kthirty.extra.oos.storage;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import top.kthirty.extra.oos.config.OosProperties;
import top.kthirty.extra.oos.enums.OosStorageType;

import java.io.ByteArrayInputStream;

/**
 * SeaweedFS Filer 存储（HTTP 方式）
 *
 * @author KThirty
 */
@RequiredArgsConstructor
public class SeaweedFsStorageClient implements OosStorageClient {

    private final OosProperties properties;

    @Override
    public OosStorageType getType() {
        return OosStorageType.SEAWEEDFS;
    }

    @Override
    public StorageObject upload(UploadRequest request) {
        String url = buildFilerUrl(request.getObjectKey());
        byte[] bytes = IoUtil.readBytes(request.getInputStream());
        HttpResponse response = HttpRequest.post(url)
                .body(bytes)
                .contentType(StrUtil.blankToDefault(request.getContentType(), "application/octet-stream"))
                .execute();
        if (!response.isOk()) {
            throw new IllegalStateException("SeaweedFS upload failed: " + response.getStatus() + " " + response.body());
        }
        return StorageObject.builder()
                .objectKey(request.getObjectKey())
                .bucket(properties.getSeaweedfs().getCollection())
                .size(bytes.length)
                .contentType(request.getContentType())
                .build();
    }

    @Override
    public StorageObject download(String objectKey) {
        String url = buildFilerUrl(objectKey);
        HttpResponse response = HttpUtil.createGet(url).execute();
        if (!response.isOk()) {
            throw new IllegalArgumentException("SeaweedFS file not found: " + objectKey);
        }
        byte[] bytes = response.bodyBytes();
        return StorageObject.builder()
                .objectKey(objectKey)
                .bucket(properties.getSeaweedfs().getCollection())
                .inputStream(new ByteArrayInputStream(bytes))
                .size(bytes.length)
                .contentType(response.header("Content-Type"))
                .build();
    }

    @Override
    public void delete(String objectKey) {
        String url = buildFilerUrl(objectKey);
        HttpResponse response = HttpRequest.delete(url).execute();
        if (!response.isOk() && response.getStatus() != 404) {
            throw new IllegalStateException("SeaweedFS delete failed: " + response.getStatus());
        }
    }

    private String buildFilerUrl(String objectKey) {
        String filerUrl = StrUtil.removeSuffix(properties.getSeaweedfs().getFilerUrl(), "/");
        String collection = StrUtil.removePrefix(StrUtil.removeSuffix(properties.getSeaweedfs().getCollection(), "/"), "/");
        String key = StrUtil.removePrefix(objectKey, "/");
        return filerUrl + "/" + collection + "/" + key;
    }
}
