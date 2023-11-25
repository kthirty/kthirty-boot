package top.kthirty.examples.db.test;

import cn.hutool.core.lang.Console;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.dict.DictUtil;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.examples.db.mapper.AccountMapper;
import top.kthirty.examples.db.model.table.AccountTableDef;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class DbStartListener implements ApplicationRunner {
    private final AccountMapper accountMapper;
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        accountMapper.selectListByQuery(QueryWrapper.create()
                        .where(AccountTableDef.ACCOUNT.USER_NAME.isNotNull()))
                .forEach(item -> {
                    Console.log(JsonUtil.toJson(item));
                    Map<String, Object> map = JsonUtil.toMap(JsonUtil.toJson(item));
                    Console.log(map.get("typeLabel"), DictUtil.getValue("type", Func.toStr(map.get("typeLabel"))));
                });

    }
}
