package top.kthirty.flowable.config;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.StaticLog;
import org.flowable.common.engine.impl.cfg.IdGenerator;

public class FlowableIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        String snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr();
        StaticLog.info("FlowableIdGenerator {}",snowflakeNextIdStr);
        return snowflakeNextIdStr;
    }
}
