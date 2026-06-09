package top.kthirty.extra.oos.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.db.base.service.BaseServiceImpl;
import top.kthirty.core.web.utils.WebUtil;
import top.kthirty.extra.oos.config.OosProperties;
import top.kthirty.extra.oos.entity.OosFile;
import top.kthirty.extra.oos.mapper.OosFileMapper;
import top.kthirty.extra.oos.model.OosFileVO;
import top.kthirty.extra.oos.service.OosFileService;
import top.kthirty.extra.oos.storage.OosStorageClient;
import top.kthirty.extra.oos.util.OosUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 对象存储文件服务实现
 *
 * @author KThirty
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OosFileServiceImpl extends BaseServiceImpl<OosFileMapper, OosFile> implements OosFileService {

    private final OosProperties properties;
    private final OosStorageClient storageClient;

    @Override
    public OosFileVO upload(MultipartFile file) {
        OosUtil.validateFile(file, properties);
        try {
            byte[] bytes = file.getBytes();
            String md5 = OosUtil.md5(new ByteArrayInputStream(bytes));
            String objectKey = OosUtil.buildObjectKey(file.getOriginalFilename());
            String contentType = StrUtil.blankToDefault(file.getContentType(), "application/octet-stream");

            OosStorageClient.StorageObject stored = storageClient.upload(OosStorageClient.UploadRequest.builder()
                    .inputStream(new ByteArrayInputStream(bytes))
                    .objectKey(objectKey)
                    .contentType(contentType)
                    .size(bytes.length)
                    .build());

            OosFile entity = OosFile.builder()
                    .originalFileName(file.getOriginalFilename())
                    .objectKey(objectKey)
                    .fileExt(OosUtil.extractExtension(file.getOriginalFilename()))
                    .fileSize((long) bytes.length)
                    .contentType(contentType)
                    .storageType(storageClient.getType().getValue())
                    .bucket(stored.getBucket())
                    .md5(md5)
                    .build();
            save(entity);
            return toVO(entity);
        } catch (Exception e) {
            throw new IllegalStateException("File upload failed", e);
        }
    }

    @Override
    public List<OosFileVO> uploadBatch(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Upload files cannot be empty");
        }
        List<OosFileVO> result = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                result.add(upload(file));
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public OosFileVO getInfo(Serializable id) {
        OosFile entity = getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("File not found");
        }
        return toVO(entity);
    }

    @Override
    public void download(Serializable id) {
        OosFile entity = getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("File not found");
        }
        OosStorageClient.StorageObject storageObject = storageClient.download(entity.getObjectKey());
        HttpServletResponse response = WebUtil.getResponse();
        try (InputStream inputStream = storageObject.getInputStream()) {
            response.reset();
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String fileName = URLEncoder.encode(entity.getOriginalFileName(), StandardCharsets.UTF_8);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", String.valueOf(storageObject.getSize()));
            response.setContentType(StrUtil.blankToDefault(storageObject.getContentType(), "application/octet-stream"));
            IoUtil.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new IllegalStateException("File download failed", e);
        }
    }

    @Override
    public boolean remove(Serializable id) {
        OosFile entity = getById(id);
        if (entity == null) {
            return false;
        }
        storageClient.delete(entity.getObjectKey());
        return removeById(id);
    }

    private OosFileVO toVO(OosFile entity) {
        return OosFileVO.builder()
                .id(entity.getId())
                .fileName(entity.getOriginalFileName())
                .url(OosUtil.buildDownloadUrl(properties, entity.getId()))
                .contentType(entity.getContentType())
                .fileSize(entity.getFileSize())
                .createDate(entity.getCreateDate())
                .build();
    }
}
