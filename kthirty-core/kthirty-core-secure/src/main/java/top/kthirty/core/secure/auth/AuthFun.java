package top.kthirty.core.secure.auth;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import top.kthirty.core.boot.secure.SecureUtil;

import java.util.List;

/**
 * 权限判断
 *
 * @author Kthirty
 */
public class AuthFun {

	/**
	 * 放行所有请求
	 *
	 * @return {boolean}
	 */
	public boolean permitAll() {
		return true;
	}

	/**
	 * 只有超管角色才可访问
	 *
	 * @return {boolean}
	 */
	public boolean denyAll() {
		return hasRole(AuthConstant.ADMIN);
	}

	/**
	 * 判断是否有该角色权限
	 *
	 * @param role 单角色
	 * @return {boolean}
	 */
	public boolean hasRole(String role) {
		return hasAnyRole(role);
	}

	/**
	 * 判断是否有该角色权限
	 *
	 * @param roles 角色集合
	 * @return {boolean}
	 */
	public boolean hasAnyRole(String... roles) {
		Assert.notEmpty(roles,"roles can`t be empty");
		return CollUtil.containsAny(SecureUtil.getRoles(),List.of(roles));
	}
	public boolean hasPermission(String permission) {
		return hasAnyPermission(permission);
	}
	public boolean hasAnyPermission(String... permission) {
		Assert.notEmpty(permission,"permission can`t be empty");
		return CollUtil.containsAny(SecureUtil.getPermissions(),List.of(permission));
	}
	public boolean hasAllPermission(String... permission) {
		Assert.notEmpty(permission,"permission can`t be null");
		return CollUtil.containsAll(SecureUtil.getPermissions(),List.of(permission));
	}

}
