package top.kthirty.flowable.config;

import cn.hutool.core.util.IdUtil;
import org.flowable.common.engine.impl.cfg.IdGenerator;
/**
 * @description flowable id生成器
 * @author KThirty
 * @since 2024/11/27 12:57
 */
public class FlowableIdGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return IdUtil.getSnowflakeNextIdStr();
    }
}
