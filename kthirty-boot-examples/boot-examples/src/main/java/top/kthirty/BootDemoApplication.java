package top.kthirty;

import cn.hutool.core.lang.Console;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.tool.Func;

@SpringBootApplication
public class BootDemoApplication {
    
    public static void main(String[] args) {
        KthirtyApplication.run(BootDemoApplication.class,args);
        Console.log(Func.join(",","1","2"));

    }
}