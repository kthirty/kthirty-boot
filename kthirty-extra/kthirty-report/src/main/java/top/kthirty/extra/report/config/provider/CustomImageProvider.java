package top.kthirty.extra.report.config.provider;

import com.bstek.ureport.provider.image.ImageProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
/**
 * 图片处理
 * @author KThirty
 * @since 2025/7/23 11:12
 */
public class CustomImageProvider implements ImageProvider {
    @Override
    public InputStream getImage(String path) {
        try {
            // 支持base64
            if (path != null && path.startsWith("data:image/")) {
                int base64Index = path.indexOf("base64,");
                if (base64Index > 0) {
                    String base64 = path.substring(base64Index + 7);
                    byte[] bytes = Base64.getDecoder().decode(base64);
                    return new ByteArrayInputStream(bytes);
                }
            }
            // 支持本地文件路径
            if (path != null && (path.startsWith("file:/") || new File(path).exists())) {
                File file = path.startsWith("file:/") ? new File(path.substring(6)) : new File(path);
                if (file.exists()) {
                    return new FileInputStream(file);
                }
            }
            // 支持网络图片
            if (path != null && (path.startsWith("http://") || path.startsWith("https://"))) {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    return conn.getInputStream();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean support(String path) {
        return path != null && (
                path.startsWith("data:image/") ||
                path.startsWith("file:/") ||
                new File(path).exists() ||
                path.startsWith("http://") ||
                path.startsWith("https://")
        );
    }

    public String getName() {
        return "custom-image-provider";
    }
}
