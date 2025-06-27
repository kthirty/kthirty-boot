package top.kthirty.dev;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.develop.DevelopApplication;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.util.DevHelper;

@KthirtyTest(appName = "system", classes = DevelopApplication.class)
@Slf4j
public class DevTest extends BaseKthirtyTest {

    @Test
    public void testTableInfo(){
        DevForm devForm = DevHelper.importTable("sys_user");
        log.info("devForm: {}", JsonUtil.toJson(devForm));

    }
}
