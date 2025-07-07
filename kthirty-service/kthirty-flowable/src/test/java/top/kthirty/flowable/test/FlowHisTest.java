package top.kthirty.flowable.test;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.model.FlowHisTask;
import top.kthirty.flowable.model.FlowProcessInstQuery;

import java.util.List;

@Slf4j
@KthirtyTest(appName = "flowable", classes = FlowableApplication.class)
public class FlowHisTest extends BaseKthirtyTest {
    @Autowired
    private ProcessInstanceController processInstanceController;

    @Test
    public void test() {
        log.info("hisDiagram {}",processInstanceController.hisDiagram("1862018672193839104"));
    }

    @Test
    public void test1(){
        List<FlowHisTask> historicTaskInstances = processInstanceController.hisTask("1862018672193839104");
        log.info("historicTaskInstances {}", JsonUtil.toJson(historicTaskInstances));
    }

    @Test
    public void test2(){
        Page<?> page = processInstanceController.page(new FlowProcessInstQuery());
        log.info("historicTaskInstances {}", JsonUtil.toJson(page));
    }
}
