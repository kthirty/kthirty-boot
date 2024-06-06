package top.kthirty.cache;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.cache.Cache;
import top.kthirty.system.SystemApplication;
import top.kthirty.system.entity.Dept;
import top.kthirty.system.service.DeptService;

@KthirtyTest(appName = "system", classes = SystemApplication.class)
@Slf4j
public class CacheTest extends BaseKthirtyTest {
    @Autowired
    private DeptService deptService;

    @Test
    public void test1(){
        Cache.keys("*").forEach(it -> {
            log.info("{}===>{}",it,Cache.get(it));
        });
        Dept dept = new Dept();
        dept.setCategory(10);
        dept.setSort(0);
        dept.setName(RandomUtil.randomStringUpper(2));
        dept.setParentId("0");
        deptService.save(dept);
        Cache.keys("*").forEach(it -> {
            log.info("{}===>{}",it,Cache.get(it));
        });
    }
}
