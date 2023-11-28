package top.kthirty.examples.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
@MapperScan(basePackages = "top.kthirty.**.mapper")
public class DemoWebApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(DemoWebApplication.class,args);
    }
}
