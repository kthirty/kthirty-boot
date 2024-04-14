package top.kthirty.system;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.system.relation.entity.RoleMenuRl;
import top.kthirty.system.relation.service.MpRoleMenuRlService;
import top.kthirty.system.relation.service.RoleMenuRlService;

import java.util.ArrayList;
import java.util.List;

@KthirtyTest(appName = "kthirty-system",classes = SystemApplication.class)
@Slf4j
public class DbTest extends BaseKthirtyTest {
    @Autowired
    private MpRoleMenuRlService mpRoleMenuRlService;
    @Autowired
    private RoleMenuRlService roleMenuRlService;


//    @Test
    public void test1(){
        TimeInterval timer = DateUtil.timer();
        timer.start();
        roleMenuRlService.saveBatch(get());
        log.info("test1 {}",timer.intervalPretty());
    }
    @Test
    public void test2(){
        TimeInterval timer = DateUtil.timer();
        timer.start();
        mpRoleMenuRlService.saveBatch(get());
        log.info("test2 {}",timer.intervalPretty());
    }

    public List<RoleMenuRl> get(){
        List<RoleMenuRl> roleMenuRls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RoleMenuRl roleMenuRl = new RoleMenuRl();
            roleMenuRl.setMenuId(RandomUtil.randomString(2));
            roleMenuRl.setRoleCode(RandomUtil.randomString(3));
            roleMenuRls.add(roleMenuRl);
        }
        return roleMenuRls;
    }
}
