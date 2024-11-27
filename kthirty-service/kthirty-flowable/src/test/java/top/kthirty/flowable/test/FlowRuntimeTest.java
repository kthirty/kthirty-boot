package top.kthirty.flowable.test;

import com.mybatisflex.core.paginate.Page;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.boot.secure.SysUserProvider;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.flowable.FlowableApplication;
import top.kthirty.flowable.controller.ProcessInstanceController;
import top.kthirty.flowable.controller.RuntimeController;
import top.kthirty.flowable.model.FlowButton;
import top.kthirty.flowable.model.FlowTask;
import top.kthirty.flowable.model.FlowTodoQuery;

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
        processInstanceController.delete("1861636743879684096","测试");
    }
    @Test
    public void testTodo(){
        SecureUtil.setSysUserProvider(() -> new SysUser().setUsername("admin").setDeptCodes(List.of("A01A01","A01A02")).setRoles(List.of("ADMIN","TEST_USER")).setIdentityCodes(List.of("A01A01:PM")));
        Page<FlowTask> todo = runtimeController.todo(new FlowTodoQuery());
        System.out.println(todo);
    }

    @Test
    public void testButtons(){
        List<FlowButton> flowButtons = runtimeController.handleButtons("1861636743921627136");
        System.out.println(flowButtons);
    }
}
