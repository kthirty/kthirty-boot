package top.kthirty.core.web.error;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import top.kthirty.core.boot.launch.LauncherService;
import top.kthirty.core.tool.api.R;


/**
 * <p>
 * 错误处理器
 * </p>
 *
 * @author KThirty
 * @since 2023/2/3
 */
public interface ErrorHandler extends Ordered, Comparable<LauncherService> {
    /**
     * 是否支持当前类型
     * @param e 异常
     * @return true if supported
     */
    boolean support(Throwable e);

    /**
     * 处理异常
     * @param e 异常
     * @param response 当前响应
     * @return result
     */
    R handle(Throwable e, HttpServletResponse response);


    /**
     * 获取排列顺序，越小越靠前
     *
     * @return order
     */
    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * 对比排序默认实现
     *
     * @param o LauncherService
     * @return compare
     */
    @Override
    default int compareTo(LauncherService o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }


}
