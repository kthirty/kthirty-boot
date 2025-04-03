package top.kthirty.examples.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
public class DemoWebApplication {
    public static void main(String[] args) {
        KthirtyApplication.run(DemoWebApplication.class,args);
    }
}
