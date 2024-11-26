package top.kthirty.flowable.test;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.controller.RuntimeController;

import java.util.List;

@Slf4j
@KthirtyTest(appName = "flowable",classes = FlowableApplication.class)
public class FlowRuntimeTest extends BaseKthirtyTest {
    @Autowired
    private RuntimeController runtimeController;
    @Autowired
    private ProcessInstanceController processInstanceController;
    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testStartProcess(){
        runtimeController.start("test", "business_id_1");
    }

    @Test
    public void testDeleteProcess(){
        processInstanceController.delete("1861331906935857152","测试");
    }
    @Test
    public void testDeleteProcess2(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery()
                .list();
        list.forEach(it -> {
            runtimeService.deleteProcessInstance(it.getId(),"测试");
        });
    }
}
