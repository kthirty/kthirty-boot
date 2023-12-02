package top.kthirty.core.secure.interceptor;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * secure api放行配置
 *
 * @author Kthirty
 */
@Data
public class SecureRegistry {

	private boolean enabled = true;

	private final List<String> defaultExcludePatterns = new ArrayList<>();

	private final List<String> excludePatterns = new ArrayList<>();

	public SecureRegistry() {
		this.defaultExcludePatterns.add("/actuator/health/**");
		this.defaultExcludePatterns.add("/error/**");
		this.defaultExcludePatterns.add("/assets/**");
		this.defaultExcludePatterns.add("/favicon.ico");
		// swagger
		this.defaultExcludePatterns.add("/doc.html");
		this.defaultExcludePatterns.add("/webjars/**");
		this.defaultExcludePatterns.add("/v3/api-docs/**");
	}

	/**
	 * 设置放行api
	 */
	public SecureRegistry excludePathPatterns(String... patterns) {
		return excludePathPatterns(Arrays.asList(patterns));
	}

	/**
	 * 设置放行api
	 */
	public SecureRegistry excludePathPatterns(List<String> patterns) {
		this.excludePatterns.addAll(patterns);
		return this;
	}

}
