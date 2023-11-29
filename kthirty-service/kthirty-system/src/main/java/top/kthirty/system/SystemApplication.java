package top.kthirty.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
@MapperScan
public class SystemApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(SystemApplication.class,args);

    }
}
