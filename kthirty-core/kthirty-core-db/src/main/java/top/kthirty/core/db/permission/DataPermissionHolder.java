package top.kthirty.core.db.permission;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

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
    public static DataPermissionContext getContext(){
        if(CONTEXT.get() != null){
            return CONTEXT.get();
        }
        DataPermissionContext dataPermissionContext = new DataPermissionContext();
        setContext(dataPermissionContext);
        return dataPermissionContext;
    }
    public static void setContext(DataPermissionContext context){
        CONTEXT.set(context);
    }
    public static void clearContext(){
        CONTEXT.remove();
    }

    public static void handle() {
        // TODO 实际逻辑
        log.info("处理数据权限: {}",getContext());
        DataPermissionContext context = getContext();

        context.setProcessed(true);
    }
}
