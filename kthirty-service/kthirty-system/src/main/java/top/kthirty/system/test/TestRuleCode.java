package top.kthirty.system.test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.kthirty.core.tool.utils.RuleCodeUtil;
@Component
public class TestRuleCode implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < RandomUtil.randomInt(10,500); i++) {
            String next = RuleCodeUtil.getInstance(RuleCodeUtil.HandlerPool.SINGLE_LETTER, "sys_role:code").next();
            Console.log(">>",next);
            for (int j = 0; j < RandomUtil.randomInt(10,500); j++) {
                String childNext = RuleCodeUtil.getInstance(RuleCodeUtil.HandlerPool.SINGLE_LETTER, "sys_role:code").next(next);
                Console.log(">>>>",childNext);
                for (int j1 = 0; j1 < RandomUtil.randomInt(10,500); j1++) {
                    String childNext1 = RuleCodeUtil.getInstance(RuleCodeUtil.HandlerPool.SINGLE_LETTER, "sys_role:code").next(childNext);
                    Console.log(">>>>>>",childNext1);
                }
            }

        }
    }
}
