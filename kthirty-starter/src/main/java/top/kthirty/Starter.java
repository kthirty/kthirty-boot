package top.kthirty;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
@EnableAutoTable
// E2E-BUILD-TEST: 验证源码变更自动构建，验证后移除
public class Starter {
    public static void main(String[] args) {
        KthirtyApplication.run(Starter.class,args);
    }
}
