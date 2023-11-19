package top.kthirty.core.tool.redisson;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@AllArgsConstructor
@Slf4j
public class GlobalLockAspect {
    private final RedissonClient redissonClient;
    @Around("@annotation(GlobalLock)")
    public Object runWithLock(ProceedingJoinPoint point) throws Throwable {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        GlobalLock globalLock = AnnotationUtil.getAnnotation(method, GlobalLock.class);
        String name = globalLock.value();
        Assert.notBlank(name, "GlobalLock name must not be blank");
        log.debug("Redisson分布式锁[{}]尝试获取锁",name);
        RLock lock = redissonClient.getLock(name);
        lock.lock(globalLock.leaseTime(), TimeUnit.SECONDS);
        try {
            log.debug("Redisson分布式锁[{}]获取锁成功,执行业务逻辑",name);
            return point.proceed();
        } finally {
            log.debug("Redisson分布式锁[{}]执行业务结束,释放锁",name);
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
