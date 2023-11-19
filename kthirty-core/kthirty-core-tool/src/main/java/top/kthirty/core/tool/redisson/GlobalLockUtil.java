package top.kthirty.core.tool.redisson;

import cn.hutool.core.lang.func.VoidFunc0;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import top.kthirty.core.tool.utils.SpringUtil;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 全局锁Redisson
 * </p>
 *
 * @author KThirty
 * @since 2023/1/12
 */
@Slf4j
public class GlobalLockUtil {
    private static RedissonClient redissonClient;
    public static void run(String lockName,VoidFunc0 func0){
        if(redissonClient == null){
            redissonClient = SpringUtil.getBean(RedissonClient.class);
        }
        RLock lock = redissonClient.getLock(lockName);
        lock.lock(30, TimeUnit.SECONDS);
        try {
            func0.callWithRuntimeException();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public static void tryRun(String lockName,VoidFunc0 func0){
        if(redissonClient == null){
            redissonClient = SpringUtil.getBean(RedissonClient.class);
        }
        RLock lock = redissonClient.getLock(lockName);
        try {
            boolean locked = lock.tryLock(30, TimeUnit.SECONDS);
            if(locked){
                func0.callWithRuntimeException();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while trying to lock ",e);
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
