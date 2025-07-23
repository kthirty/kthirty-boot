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
    private static SysUserProvider sysUserProvider;
    public static final String SUPER_ADMIN_CODE = "administrator";
    public static String getUserId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }
    public static String getUsername(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getUsername() : null;
    }
    public static String getRealName(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getRealName() : null;
    }
    public static String getOrgCode(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getOrgCode() : null;
    }
    public static List<String> getDeptCodes(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getDeptCodes() : List.of();
    }
    public static String getTenantId(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null && CollUtil.isNotEmpty(currentUser.getTenantIds()) ? currentUser.getTenantIds().get(0) : null;
    }
    public static List<String> getPermissions(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getPermissions() : List.of();
    }
    public static List<String> getRoles(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getRoles() : List.of();
    }
    public static List<String> getIdentityCodes(){
        SysUser currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getIdentityCodes() : List.of();
    }
    public static boolean isSuperAdmin(){
        return getRoles().contains(SUPER_ADMIN_CODE);
    }
    public static boolean isNotSuperAdmin(){
        return !isSuperAdmin();
    }
    public static SysUser getCurrentUser(){
        SysUserProvider provider = getProvider();
        if(provider != null){
            return provider.getUser();
        }
        return null;
    }
    public static SysUserProvider getProvider(){
        if(sysUserProvider != null){
            return sysUserProvider;
        }
        if(context == null){
            return null;
        }
        try{
            sysUserProvider = context.getBean(SysUserProvider.class);
            return sysUserProvider;
        }catch (Throwable ignore){}
        return null;
    }
    public static void setSysUserProvider(SysUserProvider sysUserProvider){
        SecureUtil.sysUserProvider = sysUserProvider;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SecureUtil.context = applicationContext;
    }
    private static ApplicationContext context;
}
