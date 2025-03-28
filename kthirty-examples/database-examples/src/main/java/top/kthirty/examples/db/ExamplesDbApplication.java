package top.kthirty.examples.db;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan
@EnableAutoTable
public class ExamplesDbApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(ExamplesDbApplication.class,args);
    }
}
