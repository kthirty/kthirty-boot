package top.kthirty.core.boot.launch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 应用信息
 * </p>
 *
 * @author Kthirty
 * @since 2023/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KthirtyAppInfo {
    /**
     * 服务名，用于默认${spring.application.name}
     */
    private String applicationName;
    /**
     * 描述
     */
    private String description;
    /**
     * 默认运行端口
     */
    private Integer port = 8080;
    /**
     * 自定义处理器
     */
    private List<LauncherService> customLaunchers = new ArrayList<>();

    public KthirtyAppInfo(String applicationName, String description, Integer port) {
        this.applicationName = applicationName;
        this.description = description;
        this.port = port;
    }

    public void addLauncher(LauncherService... expandLaunchers){
        if(customLaunchers == null){
            customLaunchers = new ArrayList<>(expandLaunchers.length);
        }
        this.customLaunchers.addAll(Arrays.asList(expandLaunchers));
    }
}
