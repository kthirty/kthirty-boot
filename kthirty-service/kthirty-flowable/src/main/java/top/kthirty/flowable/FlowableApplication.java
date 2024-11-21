package top.kthirty.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@MapperScan(basePackages = "top.kthirty.**.mapper.*Mapper")
public class FlowableApplication implements SwaggerRegister {
    public static void main(String[] args) {
        RepositoryService repositoryService = null;
        Model model = repositoryService.newModel();

        ModelQuery modelQuery = repositoryService.createModelQuery();

        KthirtyApplication.run(FlowableApplication.class,args);
    }
}
