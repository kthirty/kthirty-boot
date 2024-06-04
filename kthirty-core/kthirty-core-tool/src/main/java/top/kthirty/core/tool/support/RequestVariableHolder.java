package top.kthirty.core.tool.support;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求阶段ThreadLocal
 * 请求结束时自动清空
 * @author KThirty
 * @since 2024/6/4 15:16
 */
@Slf4j
public class RequestVariableHolder {
    private static final ThreadLocal<Kv> THREAD_LOCAL = ThreadUtil.createThreadLocal(true);

    public static void add(String key,Object value){
        init();
        THREAD_LOCAL.get().set(key,value);
    }
    @SuppressWarnings("unchecked")
    public static <T> T get(String key){
        init();
        return (T) THREAD_LOCAL.get().get(key);
    }

    public static void clear(){
        init();
        THREAD_LOCAL.get().clear();
    }
    public static boolean has(String key){
        init();
        return THREAD_LOCAL.get().containsKey(key);
    }

    private static void init(){
        if(THREAD_LOCAL.get() == null){
            THREAD_LOCAL.set(Kv.init());
        }
    }
}
