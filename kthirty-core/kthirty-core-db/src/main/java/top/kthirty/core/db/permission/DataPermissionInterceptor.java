package top.kthirty.core.db.permission;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.constant.SqlConsts;
import com.mybatisflex.core.dialect.OperateType;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.utils.StringPool;

import java.sql.Connection;
/**
 * <p>
 * 数据权限控制
 * 除MybatisFlex自带的BaseMapper以外自定义的SQL需要使用这里进行拦截修改
 * {@link DataPermissionDialectImpl#prepareAuth}
 * </p>
 *
 * @author KThirty
 * @since 2024/6/8
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class DataPermissionInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(!DataPermissionHolder.getContext().isProcessed()) {
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            BoundSql boundSql = statementHandler.getBoundSql();
            StringBuilder sql = new StringBuilder();
            OperateType operateType = OperateType.valueOf(StrUtil.subBefore(boundSql.getSql(), StringPool.SPACE, false).toUpperCase());
            DataPermissionHolder.getContext()
                    .setOperateType(operateType)
                    .setTables(null)
                    .setCurrentUser(SecureUtil.getCurrentUser())
                    .setSql(sql);
            DataPermissionHolder.handle();
            // 实际业务处理支持拼接SQL
            StringBuilder finalSql = new StringBuilder(boundSql.getSql());
            String appendSql = StrUtil.removePrefix(sql.toString().trim(), "and");
            if(StrUtil.isNotBlank(appendSql)){
                finalSql.append(SqlConsts.AND).append(appendSql);
            }
            ReflectUtil.setFieldValue(boundSql, "sql", finalSql.toString());
        }
        return invocation.proceed();
    }
}
