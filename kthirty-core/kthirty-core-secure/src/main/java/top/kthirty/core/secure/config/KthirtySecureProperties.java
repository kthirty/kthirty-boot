package top.kthirty.core.secure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * secure放行额外配置
 *
 * @author Kthirty
 */
@Getter
@Setter
@ConfigurationProperties("kthirty.secure")
@RefreshScope
public class KthirtySecureProperties {
	private boolean enabled = true;
	/**
	 * 跳过验证的url
	 */
	private List<String> skipUrl = new ArrayList<>();
	/**
	 * 单位为秒 accessToken 有效时间默认为12*60*60
	 */
	private int accessTokenValidity = 12*60*60;
	/**
	 * 单位为秒 refreshToken 有效时间默认为7*24*60*60
	 */
	private int refreshTokenValidity = 7*24*60*60;

}
