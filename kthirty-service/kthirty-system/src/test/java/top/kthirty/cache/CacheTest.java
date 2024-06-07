package top.kthirty.cache;

import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.system.SystemApplication;
import top.kthirty.system.entity.Dept;
import top.kthirty.system.service.DeptService;

import java.util.List;

@KthirtyTest(appName = "system", classes = SystemApplication.class)
@Slf4j
public class CacheTest extends BaseKthirtyTest {
    @Autowired
    private DeptService deptService;

    @Test
    public void test1(){
        List<Dept> list = deptService.list(QueryWrapper.create());
        log.info("list {}", JSONUtil.toJsonPrettyStr(list));

        boolean b = deptService.removeById("37160876074000124");
        System.out.println("b: "+b);

    }
}
