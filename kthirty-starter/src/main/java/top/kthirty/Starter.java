package top.kthirty;

import org.dromara.autotable.springboot.EnableAutoTable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
@EnableAutoTable
public class Starter {
    public static void main(String[] args) {
        KthirtyApplication.run(Starter.class,args);
    }
}
