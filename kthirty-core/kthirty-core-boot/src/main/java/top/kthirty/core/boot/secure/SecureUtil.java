package top.kthirty.core.boot.secure;

import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.List;
/**
 * <p>
 * 获取当前登录人的信息，实际逻辑由外部实现
 * @see SysUserProvider
 * </p>
 *
 * @author KThirty
 * @since 2023/11/27
 */
public class SecureUtil implements ApplicationContextAware {
    public static String getUserId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }
    public static String getRealName(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getRealName() : null;
    }
    public static String getOrgCode(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getOrgCode() : null;
    }
    public static String getTenantId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null && CollUtil.isNotEmpty(currentUser.getTenantIds()) ? currentUser.getTenantIds().get(0) : null;
    }
    public static List<String> getPermissions(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getPermissions() : null;
    }
    public static List<String> getRoles(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getRoles() : null;
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
