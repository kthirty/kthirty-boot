package top.kthirty.core.boot.props;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * 自定义配置信息
 * @author Kthirty
 * @since Created in 2023/11/16
 */
@ConfigurationProperties("kthirty")
@Data
@ToString
@RefreshScope
public class KthirtyProperties {
    /**
     * 环境
     */
    private String env;
    /**
     * 应用名
     */
    private String name;

}
