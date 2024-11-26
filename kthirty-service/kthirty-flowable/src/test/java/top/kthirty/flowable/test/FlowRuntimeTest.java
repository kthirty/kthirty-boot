package top.kthirty.flowable.test;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.controller.RuntimeController;

@Slf4j
@KthirtyTest(appName = "flowable",classes = FlowableApplication.class)
public class FlowRuntimeTest extends BaseKthirtyTest {
    @Autowired
    private RuntimeController runtimeController;
    @Autowired
    private ProcessInstanceController processInstanceController;

    @Test
    public void testTran(){
        runtimeController.testTran();
    }
    @Test
    public void testStartProcess(){
        ProcessInstance start = runtimeController.start("test", "business_id_1");
        log.info("启动结果 {}", JsonUtil.toJson(start));
    }

    @Test
    public void testDeleteProcess(){
        processInstanceController.delete("1861331906935857152","测试");
    }
}
