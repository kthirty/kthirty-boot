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
@ConfigurationProperties("kthirty.xss")
@RefreshScope
public class XssProperties {

	/**
	 * 开启xss
	 */
	private Boolean enabled = true;

	/**
	 * 放行url
	 */
	private List<String> skipUrl = new ArrayList<>();

}
