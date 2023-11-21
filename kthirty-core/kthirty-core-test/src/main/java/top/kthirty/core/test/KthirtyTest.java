package top.kthirty.core.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
/**
 * Test工具
 * @author Kthirty
 * @date Created in 2020/9/3 15:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
public @interface KthirtyTest {
    /**
     * 服务名：appName
     * @return appName
     */
    @AliasFor("appName")
    String value() default "";
    /**
     * 服务名：appName
     * @return appName
     */
    @AliasFor("value")
    String appName() default "";
    /**
     * profile
     * @return profile
     */
    String env() default "dev";
    @AliasFor(annotation = SpringBootTest.class)
    Class<?>[] classes() default {};
    @AliasFor(annotation = SpringBootTest.class)
    String[] properties() default {};
    @AliasFor(annotation = SpringBootTest.class)
    String[] args() default {};
}
