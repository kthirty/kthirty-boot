package top.kthirty.flowable.test;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.FormService;
import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.controller.TaskController;
import top.kthirty.flowable.model.FlowCompletePre;
import top.kthirty.flowable.model.FlowTask;
import top.kthirty.flowable.model.FlowTaskQuery;
import top.kthirty.flowable.model.TaskCompleteReq;

import java.util.List;

@Slf4j
@KthirtyTest(appName = "flowable", classes = FlowableApplication.class)
public class FlowRuntimeTest extends BaseKthirtyTest {
    @Autowired
    private TaskController taskController;
    @Autowired
    private ProcessInstanceController processInstanceController;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private FormService formService;

    @BeforeAll
    public static void init(){
        SecureUtil.setSysUserProvider(() -> new SysUser().setUsername("admin")
                .setDeptCodes(List.of("A01A01", "A01A02","TG"))
                .setRoles(List.of("ADMIN", "TEST_USER","A003")).setIdentityCodes(List.of("A01A01:PM")));
        Authentication.setAuthenticatedUserId(SecureUtil.getUsername());
    }

    @Test
    public void testStartProcess() {
        taskController.start("test02", "bid_05");
    }

    @Test
    public void testDeleteProcess() {
        processInstanceController.delete("1863145934385561600", "测试");
    }

    @Test
    public void testTodo() {
        Page<FlowTask> todo = taskController.todo(new FlowTaskQuery());
        System.out.println(JsonUtil.toJsonPrettyStr(todo));
    }
    @Test
    public void testDone() {
        Page<FlowTask> done = taskController.done(new FlowTaskQuery());
        log.info("done: {}", JsonUtil.toJsonPrettyStr(done));
    }

    @Test
    public void testCompletePre() {
        FlowCompletePre flowCompletePre = taskController.completePre("1863121088234549248");
        System.out.println(flowCompletePre);
    }

    @Test
    public void testComplete() {
        taskController.complete(new TaskCompleteReq().setTaskId("1863121088234549248").setResult("PASS")
                .setComment("测试"));
    }

    @Test
    public void testSuspend() {
        taskController.suspend("1861972122361118720");
    }

    @Test
    public void testActivate() {
        taskController.activate("1861972122361118720");
    }
}
