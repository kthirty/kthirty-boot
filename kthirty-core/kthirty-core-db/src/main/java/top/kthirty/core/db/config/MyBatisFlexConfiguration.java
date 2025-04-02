package top.kthirty.core.db.config;

import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.dromara.autotable.springboot.EnableAutoTable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.kthirty.core.db.base.entity.BaseEntity;
import top.kthirty.core.db.fill.EntityFillListener;
import top.kthirty.core.db.listener.OperatingListener;
import top.kthirty.core.db.permission.DataPermissionDialectImpl;
import top.kthirty.core.db.permission.DataPermissionInterceptor;
import top.kthirty.core.db.sequence.SequenceCodeListener;

/**
 * @author KTHIRTY
 * @since 2025/3/29
 * MyBatisFlex 配置
 */
@Slf4j
@Configuration
@AutoConfigureAfter(MybatisFlexAutoConfiguration.class)
public class MyBatisFlexConfiguration implements MyBatisFlexCustomizer {
    @Override
    public void customize(FlexGlobalConfig flexGlobalConfig) {
        // Id 生成策略
        FlexGlobalConfig.KeyConfig keyConfig = new FlexGlobalConfig.KeyConfig();
        keyConfig.setKeyType(KeyType.Generator);
        keyConfig.setValue(KeyGenerators.snowFlakeId);
        flexGlobalConfig.setKeyConfig(keyConfig);
        // 是否打印Banner
        flexGlobalConfig.setPrintBanner(false);
        // 插入与更新监听
        flexGlobalConfig.registerInsertListener(new EntityFillListener(),BaseEntity.class);
        flexGlobalConfig.registerUpdateListener(new EntityFillListener(),BaseEntity.class);
        // Sql审计功能
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(auditMessage -> log.debug("{} ,count {},{}ms", auditMessage.getFullSql(), auditMessage.getQueryCount(), auditMessage.getElapsedTime()));

        // 数据权限
        DialectFactory.registerDialect(DbType.MYSQL,new DataPermissionDialectImpl());
        DialectFactory.registerDialect(DbType.ORACLE,new DataPermissionDialectImpl());

    }


    @Bean
    public Interceptor interceptor(){
        return new DataPermissionInterceptor();
    }
}
