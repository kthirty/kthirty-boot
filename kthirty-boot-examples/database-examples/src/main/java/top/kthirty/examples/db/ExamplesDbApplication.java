package top.kthirty.examples.db;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

/**
 * <p>
 * 数据库测试
 * </p>
 *
 * @author KThirty
 * @since 2023/11/20
 */
@SpringBootApplication
public class ExamplesDbApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(ExamplesDbApplication.class,args);
    }
}
