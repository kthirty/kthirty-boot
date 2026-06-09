package top.kthirty.extra.oos.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.tool.Func;
import top.kthirty.extra.oos.config.OosProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/**
 * 对象存储工具
 *
 * @author KThirty
 */
public final class OosUtil {

    private OosUtil() {
    }

    public static String extractExtension(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }
        String ext = FileUtil.extName(fileName);
        return StrUtil.blankToDefault(ext, "").toLowerCase(Locale.ROOT);
    }

    public static String buildObjectKey(String originalFileName) {
        String ext = extractExtension(originalFileName);
        String datePath = DateUtil.format(DateUtil.date(), "yyyy/MM/dd");
        String fileName = IdUtil.fastSimpleUUID() + (StrUtil.isBlank(ext) ? "" : "." + ext);
        return datePath + "/" + fileName;
    }

    public static void validateFile(MultipartFile file, OosProperties properties) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Upload file cannot be empty");
        }
        if (file.getSize() > properties.getMaxFileSize()) {
            throw new IllegalArgumentException("File size exceeds limit");
        }
        String ext = extractExtension(file.getOriginalFilename());
        if (Func.isNotEmpty(properties.getAllowedExtensions()) && StrUtil.isNotBlank(ext)) {
            boolean allowed = properties.getAllowedExtensions().stream()
                    .map(item -> item.toLowerCase(Locale.ROOT))
                    .anyMatch(item -> item.equals(ext));
            if (!allowed) {
                throw new IllegalArgumentException("File extension not allowed: " + ext);
            }
        }
    }

    public static String md5(InputStream inputStream) throws IOException {
        return DigestUtil.md5Hex(inputStream);
    }

    public static String buildDownloadUrl(OosProperties properties, String fileId) {
        String prefix = StrUtil.removeSuffix(properties.getUrlPrefix(), "/");
        return prefix + "/" + fileId;
    }
}
