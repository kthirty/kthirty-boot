package top.kthirty;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;

@SpringBootApplication
public class BootDemoApplication {
    
    public static void main(String[] args) {
        KthirtyApplication.run(BootDemoApplication.class,args);
    }
}