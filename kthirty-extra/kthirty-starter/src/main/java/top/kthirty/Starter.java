package top.kthirty;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        KthirtyApplication.run(Starter.class,args);
    }
}
