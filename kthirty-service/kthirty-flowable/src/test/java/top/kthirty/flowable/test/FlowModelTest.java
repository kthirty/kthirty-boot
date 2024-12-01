package top.kthirty.flowable.test;

import cn.hutool.core.io.resource.ResourceUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ModelController;
import top.kthirty.flowable.model.FlowModel;
import top.kthirty.flowable.model.FlowModelQuery;

@Slf4j
@KthirtyTest(appName = "flowable",classes = FlowableApplication.class)
public class FlowModelTest extends BaseKthirtyTest {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ModelController modelController;

    @Test
    public void testAddModel(){
        FlowModel model = new FlowModel();
//        model.setName("测试流程02");
//        model.setKey("test");
        model.setXml(ResourceUtil.readUtf8Str("test02.bpmn"));
        modelController.save(model);
        log.info("id {} key {}",model.getId(),model.getKey());
    }

    @Test
    public void testDeploy(){
        modelController.deploy("1863096716195143680");
    }

    @Test
    public void testPage(){
        Page<Model> page = modelController.page(new FlowModelQuery());
        log.info("page {}",JsonUtil.toJsonPrettyStr(page));
    }

    @Test
    public void testDelete(){
        modelController.delete("1863146653876482048");
    }

    @Test
    public void testGetXml(){
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(() -> ResourceUtil.getStream("test.bpmn")
                , true, true, Charsets.UTF_8_NAME);
        System.out.println(bpmnModel.getMainProcess().getId());
    }
}
