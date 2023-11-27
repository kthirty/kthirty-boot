package top.kthirty.core.secure.auth;

/**
 * 系统默认角色
 *
 * @author Kthirty
 */
public class AuthConstant {

	/**
	 * 超管角色名
	 */
	public static final String ADMIN = "administrator";

	/**
	 * 放行
	 */
	public static final String PERMIT_ALL = "permitAll()";
	/**
	 * 仅超管
	 */
	public static final String DENY_ALL = "denyAll()";
	/**
	 * String.format 模板
	 */
	public static final String HAS_ALL_PERMISSION_TEMPLATE = "hasAllPermission('%s')";
	public static final String HAS_ANY_PERMISSION_TEMPLATE = "hasAnyPermission('%s')";
	public static final String HAS_ROLE_TEMPLATE = "hasRole('%s')";

	/**
	 * Token在请求头里的HeaderName
	 */
	public static final String REQUEST_AUTHORIZATION_HEADER = "Authorization";

}
