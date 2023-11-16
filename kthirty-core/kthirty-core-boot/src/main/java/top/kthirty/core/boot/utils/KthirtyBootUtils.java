package top.kthirty.core.boot.utils;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.StrUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.launch.annotations.KthirtyLauncher;
import top.kthirty.core.boot.launch.annotations.KthirtyLaunchers;
import top.kthirty.core.boot.constant.AppConstant;
import top.kthirty.core.boot.constant.EnvEnum;
import top.kthirty.core.boot.launch.LauncherService;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目工具类
 * @author Kthirty
 * @since Created in 2023/11/16
 */
public class KthirtyBootUtils {
    public static void processProperties(SpringApplicationBuilder builder, KthirtyLaunchInfo launchInfo) {
        Assert.notNull(launchInfo,"KthirtyLaunchInfo can not be null");
        String[] activeProfiles = launchInfo.getActiveProfiles().toArray(new String[0]);
        String activeProfileStr = String.join(",", launchInfo.getActiveProfiles());
        builder.profiles(activeProfiles);
        if(AppConstant.BASE_PACKAGES == null || AppConstant.BASE_PACKAGES.length == 0){
            AppConstant.BASE_PACKAGES = new String[]{ClassUtils.getPackageName(launchInfo.getSource())};
        }
        launchInfo.addProperties("spring.application.name", launchInfo.getAppName());
        launchInfo.addProperties("kthirty.name", launchInfo.getAppName());
        launchInfo.addProperties("spring.profiles.active", activeProfileStr);
        launchInfo.addProperties("kthirty.env", launchInfo.getEnv());
        launchInfo.addProperties("kthirty.base-package", AppConstant.basePackages());
        launchInfo.addProperties("logging.config","classpath:log/logback_".concat(launchInfo.getEnv()).concat(".xml"));
        launchInfo.addProperties("spring.main.allow-bean-definition-overriding","true");
        launchInfo.addProperties("server.port",launchInfo.getPort());
        builder.application().setDefaultProperties(launchInfo.getProperties());
    }

    /**
     * 处理当前运行环境
     * @param launchInfo 运行信息
     */
    public static void processEnv(SpringApplicationBuilder builder,KthirtyLaunchInfo launchInfo){
        String[] args = launchInfo.getArgs().toArray(new String[0]);
        // 使用SpringBoot规则获取配置的环境变量
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, environment.getSystemEnvironment()));
        List<String> springBootProfiles = Arrays.asList(environment.getActiveProfiles());
        // 获取本系统预设环境
        List<String> presetProfiles = Arrays.stream(EnvEnum.values()).map(EnvEnum::getValue).collect(Collectors.toList());
        presetProfiles.retainAll(springBootProfiles);
        if (presetProfiles.isEmpty()) {
            presetProfiles.add(EnvEnum.DEV_CODE.getValue());
        }
        Assert.isTrue(presetProfiles.size() == 1,"存在多个环境变量:"+StrUtil.join(",",presetProfiles));
        String env = presetProfiles.get(0);
        launchInfo.addActive(env);
        launchInfo.setEnv(env);
    }
    /**
     * 加载自定义组件
     * @param launchInfo 运行信息
     * @param builder SpringApplicationBuilder
     */
    public static void processLauncher(SpringApplicationBuilder builder,KthirtyLaunchInfo launchInfo){
        List<LauncherService> launcherList = new ArrayList<>();
        // 注解中的Launcher
        for (Annotation annotation : AnnotationUtil.getAnnotations(launchInfo.getSource(), true)) {
            if(annotation instanceof KthirtyLauncher){
                KthirtyLauncher kthirtyLauncher = (KthirtyLauncher) annotation;
                launcherList.add(Singleton.get(kthirtyLauncher.value()));
            }
            if(annotation instanceof KthirtyLaunchers){
                KthirtyLauncher[] kthirtyLaunchers = ((KthirtyLaunchers) annotation).value();
                for (KthirtyLauncher kthirtyLauncher : kthirtyLaunchers) {
                    launcherList.add(Singleton.get(kthirtyLauncher.value()));
                }
            }
        }

        // 自定义和SPI的Launcher
        launcherList.addAll(launchInfo.getCustomLaunchers());
        ServiceLoader.load(LauncherService.class).forEach(launcherList::add);

        launcherList.stream().sorted(Comparator.comparing(LauncherService::getOrder)).toList()
                .forEach(launcherService -> launcherService.launcher(builder, launchInfo));
    }

    /**
     * 打印运行信息
     * @param launchInfo 运行信息
     */
    public static void echoLaunchInfo(KthirtyLaunchInfo launchInfo) {
        System.out.println();
        System.out.printf("------------应用名[%s:%s]--------------%n", launchInfo.getAppName(),launchInfo.getDescription());
        System.out.printf("------------运行端口:%s--------------%n", launchInfo.getPort());
        System.out.printf("------------环境变量:%s--------------%n", launchInfo.getEnv());
        System.out.printf("------------Profiles:%s--------------%n", launchInfo.getActiveProfiles());
        System.out.printf("------------命令行参数:%s个，%s--------------%n", launchInfo.getArgs().size(), StrUtil.join("",launchInfo.getArgs()));
        System.out.printf("------------系统默认参数:%s个--------------%n", launchInfo.getProperties().size());
        launchInfo.getProperties().entrySet().stream().map(e -> e.getKey()+"="+e.getValue()).sorted().forEach(s -> System.out.printf("------------[%s]--------------%n",s));
        System.out.println();
    }


}
