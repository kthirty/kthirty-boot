package top.kthirty.develop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;
@SpringBootApplication
@MapperScan(basePackages = "top.kthirty.**.mapper")
public class DevelopApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(DevelopApplication.class,args);
    }
}
