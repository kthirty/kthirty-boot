package top.kthirty.extra.oos.storage;

import cn.hutool.core.io.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import top.kthirty.core.tool.Func;
import top.kthirty.extra.oos.config.OosProperties;
import top.kthirty.extra.oos.enums.OosStorageType;

import java.io.File;
import java.io.FileInputStream;

/**
 * 本地文件存储
 *
 * @author KThirty
 */
@RequiredArgsConstructor
public class LocalStorageClient implements OosStorageClient {

    private final OosProperties properties;

    @Override
    public OosStorageType getType() {
        return OosStorageType.LOCAL;
    }

    @Override
    public StorageObject upload(UploadRequest request) {
        File target = resolveFile(request.getObjectKey());
        FileUtil.mkParentDirs(target);
        FileUtil.writeFromStream(request.getInputStream(), target);
        return StorageObject.builder()
                .objectKey(request.getObjectKey())
                .size(request.getSize())
                .contentType(request.getContentType())
                .build();
    }

    @Override
    @SneakyThrows
    public StorageObject download(String objectKey) {
        File file = resolveFile(objectKey);
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + objectKey);
        }
        return StorageObject.builder()
                .objectKey(objectKey)
                .inputStream(new FileInputStream(file))
                .size(file.length())
                .contentType(FileUtil.getMimeType(objectKey))
                .build();
    }

    @Override
    public void delete(String objectKey) {
        File file = resolveFile(objectKey);
        if (file.exists()) {
            FileUtil.del(file);
        }
    }

    public File resolveFile(String objectKey) {
        String basePath = properties.getLocal().getBasePath();
        return new File(basePath, Func.toStr(objectKey).replace("\\", "/"));
    }
}
