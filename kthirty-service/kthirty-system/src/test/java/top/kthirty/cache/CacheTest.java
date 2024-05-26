package top.kthirty.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.redis.RedisUtil;
import top.kthirty.excel.TestUser;
import top.kthirty.system.SystemApplication;
import top.kthirty.system.menu.controller.MenuController;

@KthirtyTest(appName = "system", classes = SystemApplication.class)
@Slf4j
public class CacheTest extends BaseKthirtyTest {
    @Autowired
    private MenuController menuController;

    @Test
    public void test1(){
        RedisUtil.set("test",new TestUser());
    }
}
