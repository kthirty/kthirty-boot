package top.kthirty.core.db.permission;

import cn.hutool.core.thread.ThreadUtil;
/**
 * <p>
 * 当前Mapper上下文
 * </p>
 *
 * @author KThirty
 * @since 2024/6/6
 */
public class DataPermissionHolder {
    private static final ThreadLocal<DataPermissionContext> CONTEXT = ThreadUtil.createThreadLocal(true);
    public static DataPermissionContext get(){
        return CONTEXT.get() != null ? CONTEXT.get() : new DataPermissionContext();
    }
    public static void set(DataPermissionContext context){
        CONTEXT.set(context);
    }
    public static void clear(){
        CONTEXT.remove();
    }
}
