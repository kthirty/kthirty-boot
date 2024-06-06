package top.kthirty.core.db.permission;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Slf4j
public class DataPermissionAspect {
    @Pointcut("execution(* *(..)) && within(top.kthirty.core.db.base.mapper.BaseMapper+)")
    public void baseMapperMethods() {}

    @Before("baseMapperMethods()")
    public void before(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        DataPermissionHolder.set(DataPermissionContext.builder()
                .className(className)
                .methodName(methodName)
                .build());
        DataPermissionHolder.get().setParameters(args);
    }

    @After("baseMapperMethods()")
    public void after(JoinPoint joinPoint) {
        DataPermissionHolder.clear();
    }
}
