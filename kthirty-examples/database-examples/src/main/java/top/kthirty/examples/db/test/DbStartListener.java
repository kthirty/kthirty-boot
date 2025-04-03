package top.kthirty.examples.db.test;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kthirty.examples.db.mapper.AccountMapper;

import javax.sql.DataSource;

@Slf4j
@Component
@AllArgsConstructor
public class DbStartListener implements ApplicationRunner {
    private final AccountMapper accountMapper;
//    private final UserMapper userMapper;
    private final DataSource dataSource;

    @Override
    public void run(ApplicationArguments args)  {
//        userMapper.selectAll().forEach(it -> System.out.println(it));

    }
}
