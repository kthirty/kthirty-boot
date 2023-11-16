package top.kthirty.core.boot.launch;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * 启动参数
 * @author Kthirty
 * @since Created in 2023/11/16
 */
@Data
@Accessors(chain = true)
public class KthirtyLaunchInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 应用名
     */
    private String appName;
    /**
     * 默认运行端口
     */
    private int port;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 当前运行环境
     * @see top.kthirty.core.boot.constant.EnvEnum
     */
    private String env;
    private Set<String> activeProfiles = new HashSet<>();
    /**
     * 系统启动默认参数，优先级最低
     */
    private Map<String,Object> properties = new HashMap<>();
    private Class<?> source;
    /**
     * 命令行参数
     */
    private Set<String> args = new HashSet<>();
    /**
     * 自定义处理器
     */
    private List<LauncherService> customLaunchers = new ArrayList<>();

    /**
     * 添加应用启动默认参数，优先级最低
     * @param key 参数Key
     * @param value 参数value
     */
    public void addProperties(String key,Object value){
        properties.put(key,value);
    }
    public void addActive(String active){
        activeProfiles.add(active);
    }
}
