package top.kthirty.core.web.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 *
 * @author Kthirty
 */
@Data
@ConfigurationProperties("kthirty.xss.url")
@RefreshScope
public class XssUrlProperties {

	private final List<String> excludePatterns = new ArrayList<>();

}
