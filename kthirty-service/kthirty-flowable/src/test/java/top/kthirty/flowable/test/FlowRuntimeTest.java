package top.kthirty.flowable.test;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.FormService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.impl.runtime.ProcessInstanceBuilderImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.form.api.FormInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.boot.secure.SysUserProvider;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.controller.RuntimeController;
import top.kthirty.flowable.model.*;

import java.util.List;

@Slf4j
@KthirtyTest(appName = "flowable", classes = FlowableApplication.class)
public class FlowRuntimeTest extends BaseKthirtyTest {
    @Autowired
    private RuntimeController runtimeController;
    @Autowired
    private ProcessInstanceController processInstanceController;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private FormService formService;

    @BeforeAll
    public static void init(){
        SecureUtil.setSysUserProvider(() -> new SysUser().setUsername("admin").setDeptCodes(List.of("A01A01", "A01A02")).setRoles(List.of("ADMIN", "TEST_USER")).setIdentityCodes(List.of("A01A01:PM")));
        Authentication.setAuthenticatedUserId(SecureUtil.getUsername());
    }

    @Test
    public void testStartProcess() {
        runtimeController.start("test", "business_id_1");
    }

    @Test
    public void testDeleteProcess() {
        processInstanceController.delete("1862016678959919104", "测试");
    }

    @Test
    public void testTodo() {
        Page<FlowTask> todo = runtimeController.todo(new FlowTodoQuery());
        System.out.println(JsonUtil.toJsonPrettyStr(todo));
        System.out.println(todo);
    }

    @Test
    public void testCompletePre() {
        FlowCompletePre flowCompletePre = runtimeController.completePre("1861972122361118720");
        System.out.println(flowCompletePre);
    }

    @Test
    public void testComplete() {
        runtimeController.complete(new TaskCompleteReq().setTaskId("1862004034668482560").setResult("PASS").setComment("测试"));
    }

    @Test
    public void testSuspend() {
        runtimeController.suspend("1861972122361118720");
    }

    @Test
    public void testActivate() {
        runtimeController.activate("1861972122361118720");
    }
}
