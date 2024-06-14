package top.kthirty.core.db.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryTable;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.boot.secure.SysUser;
import top.kthirty.core.db.permission.support.DataPermission;
import top.kthirty.core.db.permission.support.DataPermissionHelper;
import top.kthirty.core.db.permission.support.HandleType;
import top.kthirty.core.db.permission.support.ScopeType;
import top.kthirty.core.tool.support.Kv;

import java.util.List;

/**
 * <p>
 * 当前Mapper上下文
 * </p>
 *
 * @author KThirty
 * @since 2024/6/6
 */
@Slf4j
public class DataPermissionHolder {
    private static final ThreadLocal<DataPermissionContext> CONTEXT = ThreadUtil.createThreadLocal(true);

    public static DataPermissionContext getContext() {
        if (CONTEXT.get() != null) {
            return CONTEXT.get();
        }
        DataPermissionContext dataPermissionContext = new DataPermissionContext();
        dataPermissionContext.setSql(new StringBuilder());
        dataPermissionContext.setTables(ListUtil.list(true));
        dataPermissionContext.setCurrentUser(new SysUser());
        setContext(dataPermissionContext);
        return dataPermissionContext;
    }

    public static void setContext(DataPermissionContext context) {
        CONTEXT.set(context);
    }

    public static void clearContext() {
        CONTEXT.remove();
    }

    public static void handle() {
        DataPermissionContext context = getContext();
        List<String> tableNames = context.getTables().stream().map(QueryTable::getName).toList();
        List<DataPermission> list = DataPermissionHelper.getAll()
                .stream()
                .filter(it -> {
                    // 操作方式不匹配
                    if (!StrUtil.equalsIgnoreCase(context.getOperateType().name(), it.getOperateType())) {
                        return false;
                    }
                    // METHOD验证
                    if (it.getScopeType() == ScopeType.METHOD && StrUtil.equals(it.getScopeValue(), context.getClassName() + "." + context.getMethodName())) {
                        return true;
                    }
                    // table验证
                    if (it.getScopeType() == ScopeType.TABLE && CollUtil.contains(tableNames, it.getScopeValue())) {
                        return true;
                    }
                    return false;
                }).toList();
        list.forEach(it -> {
            String script = it.getHandleType() == HandleType.RULE
                    ? DataPermissionHelper.getHandleRule(it.getHandleContent())
                    : it.getHandleContent();
            Kv param = Kv.init();
            param.set("context", context);
            param.set("param", StrUtil.format(it.getHandleParam(), Kv.init()
                    .set("current_org_code", context.getCurrentUser().getOrgCode())
                    .set("current_username", context.getCurrentUser().getUsername())));
            DataPermissionHelper.execScript(script, param);
        });
        context.setProcessed(true);
    }
}
