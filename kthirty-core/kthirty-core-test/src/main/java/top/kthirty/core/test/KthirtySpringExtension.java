package top.kthirty.core.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import top.kthirty.core.boot.launch.KthirtyLaunchInfo;
import top.kthirty.core.boot.utils.KthirtyBootUtils;

import java.util.Properties;

/**
 * 设置启动参数
 *
 * @author kthirty
 */
public class KthirtySpringExtension extends SpringExtension {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        super.beforeAll(context);
        setUpTestClass(context);
    }
    private void setUpTestClass(ExtensionContext context) {
        Class<?> clazz = context.getRequiredTestClass();
        KthirtyTest kthirtyTest = AnnotationUtils.getAnnotation(clazz, KthirtyTest.class);
        Assert.isTrue(kthirtyTest != null,String.format("%s must be @KthirtyTest .", clazz));
        Assert.hasText(kthirtyTest.appName(), "@KthirtyTest AppName must not blank");
        KthirtyLaunchInfo launchInfo = new KthirtyLaunchInfo();
        launchInfo.setAppName(kthirtyTest.appName());
        launchInfo.setSource(kthirtyTest.getClass());
        launchInfo.setEnv(kthirtyTest.env());
        launchInfo.addActive(kthirtyTest.env());
        launchInfo.setPort(8900);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(clazz);
        KthirtyBootUtils.processEnv(builder, launchInfo);
        KthirtyBootUtils.processLauncher(builder,launchInfo);
        KthirtyBootUtils.processProperties(builder,launchInfo);
        KthirtyBootUtils.echoLaunchInfo(launchInfo);
        Properties properties = System.getProperties();
        launchInfo.getProperties().forEach((k,v) -> properties.setProperty(k,String.valueOf(v)));
    }
}
