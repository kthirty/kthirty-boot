package top.kthirty.dev;

import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.develop.DevelopApplication;

import javax.sql.DataSource;

@KthirtyTest(appName = "system", classes = DevelopApplication.class)
@Slf4j
public class DevTest extends BaseKthirtyTest {

    @Test
    public void testTableInfo(){
        DataSource dataSource = SpringUtil.getBeanSafe(DataSource.class);
        Assert.notNull(dataSource,"dataSource is null");
        Table table = MetaUtil.getTableMeta(dataSource, "sys_user");
        log.info(JSONUtil.toJsonPrettyStr(table));
    }
}
