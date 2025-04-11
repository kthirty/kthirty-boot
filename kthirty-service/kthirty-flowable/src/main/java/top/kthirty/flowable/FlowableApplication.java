package top.kthirty.flowable;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

@SpringBootApplication
public class FlowableApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(FlowableApplication.class,args);
    }
}
