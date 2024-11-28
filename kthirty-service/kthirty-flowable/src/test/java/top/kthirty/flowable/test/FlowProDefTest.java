package top.kthirty.flowable.test;

import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessDefinitionController;
import top.kthirty.flowable.model.FlowProcessDefModel;
import top.kthirty.flowable.model.FlowProcessDefQuery;

@Slf4j
@KthirtyTest(classes = FlowableApplication.class, appName = "flowable")
public class FlowProDefTest extends BaseKthirtyTest {
    @Autowired
    private ProcessDefinitionController processDefinitionController;

    @Test
    public void testPage() {
        Page<FlowProcessDefModel> processDefinitionPage = processDefinitionController.defPage(new FlowProcessDefQuery());
        log.info(JsonUtil.toJsonPrettyStr(processDefinitionPage));
    }

    @Test
    public void active(){
        processDefinitionController.active("test:3:1861722169290162176");
    }
    @Test
    public void suspended(){
        processDefinitionController.suspended("test:3:1861722169290162176");
    }
    @Test
    public void get(){
        FlowProcessDefModel flowProcessDefModel = processDefinitionController.get("test:3:1861722169290162176", true, true);
        System.out.println(JSONUtil.toJsonPrettyStr(flowProcessDefModel));
    }
}
