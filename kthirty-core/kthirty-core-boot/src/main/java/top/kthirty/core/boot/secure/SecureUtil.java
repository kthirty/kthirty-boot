package top.kthirty.core.boot.secure;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

public class SecureUtil implements ApplicationContextAware {
    public static String getUserId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }
    public static String getOrgCode(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getOrgCode() : null;
    }
    public static String getTenantId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null && CollUtil.isNotEmpty(currentUser.getTenantIds()) ? currentUser.getTenantIds().get(0) : null;
    }
    public static SysUser getCurrentUser(){
        SysUserProvider provider = getProvider();
        if(provider != null){
            return provider.getUser();
        }
        return null;
    }
    public static SysUserProvider getProvider(){
        if(context == null){
            return null;
        }
        try{
            return context.getBean(SysUserProvider.class);
        }catch (Throwable ignore){}
        return null;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SecureUtil.context = applicationContext;
    }
    private static ApplicationContext context;
}
