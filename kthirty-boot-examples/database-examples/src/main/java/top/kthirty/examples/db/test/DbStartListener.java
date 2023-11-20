package top.kthirty.examples.db.test;

import cn.hutool.core.lang.Console;
import cn.hutool.json.JSONUtil;
import com.demo.mapper.Account1Mapper;
import com.demo.model.table.Account1TableDef;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kthirty.examples.db.mapper.AccountMapper;
import top.kthirty.examples.db.model.table.AccountTableDef;

import javax.sql.DataSource;
@Slf4j
@Component
@AllArgsConstructor
public class DbStartListener {
    private final AccountMapper accountMapper;
    private final Account1Mapper account1Mapper;
    private final DataSource dataSource;

    @PostConstruct
    public void init(){
        accountMapper.selectListByQuery(QueryWrapper.create()
                .where(AccountTableDef.ACCOUNT.USER_NAME.isNotNull()))
                .forEach(item -> Console.log(JSONUtil.toJsonStr(item)));
        account1Mapper.selectListByQuery(QueryWrapper.create()
                .where(Account1TableDef.ACCOUNT1.USER_NAME.isNotNull()))
                .forEach(item -> Console.log(JSONUtil.toJsonStr(item)));
    }
}
