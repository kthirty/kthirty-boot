package top.kthirty.extra.report.config.filter;

import cn.hutool.core.convert.Convert;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.api.R;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.tool.jackson.JsonUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * UReport 授权处理
 *
 * @author KTHIRTY
 */
@Slf4j
public class UReportAuthFilter implements Filter {

	public static final List<String> STATIC_RESOURCE_EXTENSIONS = Arrays.asList(
			".css", ".js", ".png", ".jpg", ".jpeg", ".gif",
			".woff", ".woff2", ".ttf", ".eot",
			".ico", ".svg",
			".html", ".htm",
			".mp4", ".mp3", ".webm",
			".txt", ".json", ".xml",
			".zip", ".rar", ".7z"
	);
	private static final String RESOURCE_PATH_PREFIX = "/ureport/res/ureport-asserts";
	private static final String DESIGNER_PATH_PREFIX = "/ureport/designer";
	private static final String SESSION_AUTH_KEY = "UREPORT_AUTH";
	private static final String SESSION_USER_KEY = "UREPORT_USER_KEY";
	private static final String SESSION_USER_ID = "UREPORT_USER_ID";
	private static final String SESSION_USER_IS_SUPER_ADMIN = "UREPORT_USER_IS_SUPER_ADMIN";
	private static final Integer INTERVAL_TIME = 10 * 60 * 60;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestUri = httpRequest.getRequestURI();

		log.info("request uri: {} Host :{}", requestUri,httpRequest.getRemoteHost());
		// 静态资源直接放行
		if (isStaticResource(requestUri)) {
			chain.doFilter(request, response);
			return;
		}
		// 未授权处理
		if (!isAuthenticatedWithSession(httpRequest)) {
			handleUnauthorized(httpRequest,httpResponse);
			return;
		}

		// 验证通过放行请求
		chain.doFilter(request, response);
	}

	/**
	 * 判断是否为静态资源文件
	 */
	private boolean isStaticResource(String url) {
		// 检查是否为资源文件路径
		if (url.startsWith(RESOURCE_PATH_PREFIX)) {
			return true;
		}

		// 检查文件扩展名
		String requestUri = url.toLowerCase();
		return STATIC_RESOURCE_EXTENSIONS.stream().anyMatch(requestUri::endsWith);
	}

	/**
	 * 验证用户是否已认证（Session + Token 双重验证）
	 */
	private boolean isAuthenticatedWithSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		// 1. 优先检查 Session 认证状态
		if (session != null && Boolean.TRUE.equals(session.getAttribute(SESSION_AUTH_KEY))) {
			log.debug("Session 认证有效，用户: {}", session.getAttribute(SESSION_USER_KEY));
			// 访问设计界面
			if(request.getRequestURI().startsWith(DESIGNER_PATH_PREFIX)){
				// 非超级管理员禁止访问设计界面
				if(!Boolean.TRUE.equals(session.getAttribute(SESSION_USER_IS_SUPER_ADMIN))){
					request.setAttribute("error", "非超级管理员禁止访问设计界面");
					return false;
				}
			}
			return true;
		}

		// 2. Session 无效，尝试 Token 认证
		if (isTokenAuthenticated()) {
			// Token 认证成功，建立 Session
			createSession(request);
			log.info("Token 认证成功，已建立 Session 会话");
			return true;
		}

		return false;
	}

	/**
	 * Token 认证验证
	 */
	private boolean isTokenAuthenticated() {
		try {
			String userId = SecureUtil.getUserId();
			return Func.isNotBlank(userId);
		} catch (Exception e) {
			log.debug("Token 认证失败: {}", e.getMessage());
			return false;
		}
	}

	/**
	 * 建立认证会话
	 */
	private void createSession(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		try {
			String userAccount = SecureUtil.getUsername();
			String userId = SecureUtil.getUserId();
			boolean superAdmin = SecureUtil.isSuperAdmin();
			if (Func.isBlank(userAccount) || Func.isBlank(userId)) {
				log.warn("无法建立 UReport 认证会话: 用户信息不完整");
				return;
			}
			// 设置会话属性
			session.setAttribute(SESSION_AUTH_KEY, true);
			session.setAttribute(SESSION_USER_KEY, userAccount);
			session.setAttribute(SESSION_USER_ID, userId);
			session.setAttribute(SESSION_USER_IS_SUPER_ADMIN, superAdmin);
			session.setMaxInactiveInterval(INTERVAL_TIME);

			log.debug("建立 UReport 认证会话: 用户 {}, SessionID: {}", userAccount, session.getId());
		} catch (Exception e) {
			log.warn("建立认证会话失败: {}", e.getMessage());
		}
	}


	/**
	 * 处理未授权访问
	 */
	private void handleUnauthorized(HttpServletRequest httpRequest,HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		try (PrintWriter writer = response.getWriter()) {
			writer.write(Objects.requireNonNull(JsonUtil.toJson(R.fail(SystemResultCode.NOT_LOGIN, Convert.toStr(httpRequest.getAttribute("error"),"未登录")))));
			writer.flush();
		}
	}

}
