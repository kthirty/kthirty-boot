package top.kthirty.core.db.config;

import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.db.base.entity.BaseEntity;
import top.kthirty.core.db.listener.OperatingListener;

@Slf4j
@Configuration
@AutoConfigureBefore(MybatisFlexAutoConfiguration.class)
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {
    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {

        // Id 生成策略
        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue(KeyGenerators.flexId);
        flexGlobalConfig.setKeyConfig(keyConfig);
        // 是否打印Banner
        flexGlobalConfig.setPrintBanner(false);
        // 插入与更新监听
        flexGlobalConfig.registerInsertListener(new OperatingListener(), BaseEntity.class);
        flexGlobalConfig.registerUpdateListener(new OperatingListener(), BaseEntity.class);
        // Sql审计功能
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(auditMessage -> log.debug("{},{}ms", auditMessage.getFullSql(), auditMessage.getElapsedTime()));
    }
}
