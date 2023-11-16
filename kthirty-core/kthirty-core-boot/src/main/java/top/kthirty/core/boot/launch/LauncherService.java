package top.kthirty.core.boot.launch;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

/**
 * 启动组件拓展
 * @author Kthirty
 * @since Created in 2023/11/16
 */
public interface LauncherService extends Ordered, Comparable<LauncherService>  {

	/**
	 * 启动时 处理 SpringApplicationBuilder
	 *
	 * @param builder SpringApplicationBuilder
	 * @param launchInfo 启动信息
	 */
	void launcher(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo);

	/**
	 * 获取排列顺序，越小越靠前
	 *
	 * @return order
	 */
	@Override
	default int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
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
