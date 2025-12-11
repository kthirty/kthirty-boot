package top.kthirty.core.boot;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import top.kthirty.core.boot.constant.AppConstant;
import top.kthirty.core.boot.launch.KthirtyAppInfo;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.utils.KthirtyBootUtils;

/**
 * 项目启动器，解决启动时的环境变量问题
 *
 * @author Kthirty
 * @since 2023/11/16
 */
public class KthirtyApplication {

    public static void addBasePackages(String basePackage){
        AppConstant.addBasePackages(basePackage);
    }
    /**
     * Create an application context
     * java -jar app.jar --spring.profiles.active=prod --server.port=2333
     *
     * @param appName application name
     * @param source  The sources
     * @return an application context created from the current state
     */
    public static ConfigurableApplicationContext run(String appName, Class<?> source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(new KthirtyAppInfo(appName, "", 8080), source, args);
        return builder.run(args);
    }

    public static ConfigurableApplicationContext run(Class<?> source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(new KthirtyAppInfo(StrUtil.toUnderlineCase(StrUtil.removeSuffixIgnoreCase(source.getSimpleName(), "application")).toUpperCase(), "", 8080), source, args);
        return builder.run(args);
    }

    public static ConfigurableApplicationContext run(KthirtyAppInfo kthirtyAppInfo, Class<?> source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(kthirtyAppInfo, source, args);
        return builder.run(args);
    }

    private static SpringApplicationBuilder createSpringApplicationBuilder(KthirtyAppInfo kthirtyAppInfo, Class<?> source, String... args) {
        return handleSpringApplicationBuilder(new SpringApplicationBuilder(), kthirtyAppInfo, source, args);
    }

    /**
     * 处理启动builder
     *
     * @param builder        源builder
     * @param kthirtyAppInfo 启动信息
     * @param source         启动类
     * @param args           启动参数
     * @return SpringApplicationBuilder
     */
    public static SpringApplicationBuilder handleSpringApplicationBuilder(SpringApplicationBuilder builder, KthirtyAppInfo kthirtyAppInfo, Class<?> source, String... args) {
        Assert.hasText(kthirtyAppInfo.getApplicationName(), "[applicationName]服务名不能为空");
        builder.sources(source);
        builder .initializers(ctx -> {
            // 注册一个 BeanDefinitionRegistryPostProcessor，屏蔽非当前启动类的 @Import
            ctx.getBeanFactory().registerSingleton("importIgnorePostProcessor",
                    new org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor() {
                        final String currentApp = "top.kthirty.Starter";

                        @Override
                        public void postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry registry) {
                            for (String name : registry.getBeanDefinitionNames()) {
                                var bd = registry.getBeanDefinition(name);
                                String beanClass = bd.getBeanClassName();
                                String source = bd.getResourceDescription();
                                if (beanClass != null
                                        && source != null
                                        && source.contains("Application")
                                        && !source.contains(currentApp)
                                        && !beanClass.endsWith("Application")) {
                                    // 屏蔽非当前启动类的 @Import 导入的 BeanDefinition
                                    registry.removeBeanDefinition(name);
                                }
                            }
                        }

                        @Override
                        public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) {
                            // 不做处理
                        }
                    });
        });


        KthirtyLaunchInfo launchInfo = new KthirtyLaunchInfo()
                .setAppName(kthirtyAppInfo.getApplicationName())
                .setSource(source)
                .setPort(kthirtyAppInfo.getPort() == null ? 8080 : kthirtyAppInfo.getPort())
                .setCustomLaunchers(CollUtil.list(true,kthirtyAppInfo.getCustomLaunchers()))
                .setDescription(kthirtyAppInfo.getDescription())
                .setArgs(CollUtil.newHashSet(args));
       KthirtyBootUtils.processPackage(launchInfo);
        // 处理当前运行环境
        KthirtyBootUtils.processEnv(builder, launchInfo);
        // 处理Launcher
        KthirtyBootUtils.processLauncher(builder, launchInfo);
        // 统一处理环境变量
        KthirtyBootUtils.processProperties(builder, launchInfo);
        // 打印运行信息
        KthirtyBootUtils.echoLaunchInfo(launchInfo);
        return builder;
    }

}
