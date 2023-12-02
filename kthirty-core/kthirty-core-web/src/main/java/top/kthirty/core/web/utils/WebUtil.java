package top.kthirty.core.web.utils;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.core.tool.utils.StringPool;
import top.kthirty.core.tool.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;

/**
 * Miscellaneous utilities for web applications.
 *
 * @author KThirty
 */
@Slf4j
public class WebUtil extends org.springframework.web.util.WebUtils {

	public static final String USER_AGENT_HEADER = "user-agent";

	public static final String UN_KNOWN = "unknown";

	/**
	 * 判断是否ajax请求
	 * spring ajax 返回含有 ResponseBody 或者 RestController注解
	 *
	 * @param handlerMethod HandlerMethod
	 * @return 是否ajax请求
	 */
	public static boolean isBody(HandlerMethod handlerMethod) {
		ResponseBody responseBody = ClassUtil.getAnnotation(handlerMethod, ResponseBody.class);
		return responseBody != null;
	}

	/**
	 * 读取cookie
	 *
	 * @param name cookie name
	 * @return cookie value
	 */
	@Nullable
	public static String getCookieVal(String name) {
		HttpServletRequest request = WebUtil.getRequest();
		Assert.notNull(request, "request from RequestContextHolder is null");
		return getCookieVal(request, name);
	}

	/**
	 * 读取cookie
	 *
	 * @param request HttpServletRequest
	 * @param name    cookie name
	 * @return cookie value
	 */
	@Nullable
	public static String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除 某个指定的cookie
	 *
	 * @param response HttpServletResponse
	 * @param key      cookie key
	 */
	public static void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 *
	 * @param response        HttpServletResponse
	 * @param name            cookie name
	 * @param value           cookie value
	 * @param maxAgeInSeconds maxage
	 */
	public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取 HttpServletRequest
	 *
	 * @return {HttpServletRequest}
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static String getClientId(){
		HttpServletRequest request = getRequest();
		return request == null ? null : request.getHeader("Client-Id");
	}

	/**
	 * 获取 HttpServletResponse
	 *
	 * @return {HttpServletResponse}
	 */
	public static HttpServletResponse getResponse() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getResponse();
	}

	/**
	 * 返回json
	 *
	 * @param response HttpServletResponse
	 * @param result   结果对象
	 */
	public static void renderJson(HttpServletResponse response, Object result) {
		renderJson(response, result, MediaType.APPLICATION_JSON_VALUE);
	}

	/**
	 * 给请求下载文件
	 * @param response HttpServletResponse
	 * @param downloadFile 文件
	 * @param fileName 文件名
	 */
	public static void renderFile(HttpServletResponse response, File downloadFile, String fileName) {
		Assert.isTrue(response != null, "response is null");
		Assert.isTrue(downloadFile != null && downloadFile.exists(), "file does not exist");
		if (Func.isBlank(fileName)) {
			fileName = downloadFile.getName();
		}
		try {
			response.reset();
			response.setCharacterEncoding(CharsetUtil.UTF_8);
			response.addHeader(Header.CONTENT_DISPOSITION.getValue(), "attachment;filename=" + URLEncoder.encode(fileName, CharsetUtil.UTF_8));
			response.addHeader(Header.CONTENT_LENGTH.getValue(), "" + downloadFile.length());
			response.setContentType("application/octet-stream");
			FileUtil.writeToStream(downloadFile, response.getOutputStream());
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * ignore
	 */
	public static void renderFile(File downloadFile, String fileName) {
		renderFile(WebUtil.getResponse(), downloadFile, fileName);
	}

	/**
	 * ignore
	 */
	public static void renderFile(File downloadFile) {
		renderFile(downloadFile, null);
	}

	/**
	 * 返回json
	 *
	 * @param response    HttpServletResponse
	 * @param result      结果对象
	 * @param contentType contentType
	 */
	public static void renderJson(HttpServletResponse response, Object result, String contentType) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		try (PrintWriter out = response.getWriter()) {
			out.append(JSONUtil.toJsonStr(result));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取ip
	 *
	 * @return {String}
	 */
	public static String getIp() {
		return getIp(WebUtil.getRequest());
	}

    /**
     * 获取设备ID 前端传入的
     * @return 设备ID
     */
	public static String getDeviceId(){
		HttpServletRequest currentReq = getRequest();
		return currentReq == null ? StringPool.EMPTY : currentReq.getHeader("Kthirty-deviceId");
	}

	/**
	 * 获取ip
	 *
	 * @param request HttpServletRequest
	 * @return {String}
	 */
	@Nullable
	public static String getIp(HttpServletRequest request) {
		Assert.notNull(request, "HttpServletRequest is null");
		String ip = request.getHeader("X-Requested-For");
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (Func.isBlank(ip) || UN_KNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return Func.isBlank(ip) ? null : ip.split(",")[0];
	}

	/***
	 * 获取 request 中 json 字符串的内容
	 *
	 * @param request request
	 * @return 字符串内容
	 */
	public static String getRequestParamString(HttpServletRequest request) {
		try {
			return getRequestStr(request);
		} catch (Exception ex) {
			return StringPool.EMPTY;
		}
	}

	/**
	 * 获取 request 请求内容
	 *
	 * @param request request
	 * @return String
	 * @throws IOException IOException
	 */
	public static String getRequestStr(HttpServletRequest request) throws IOException {
		String queryString = request.getQueryString();
		if (Func.isNotBlank(queryString)) {
			return new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8).replaceAll("&amp;", "&").replaceAll("%22", "\"");
		}
		return getRequestStr(request, getRequestBytes(request));
	}

	/**
	 * 获取 request 请求的 byte[] 数组
	 *
	 * @param request request
	 * @return byte[]
	 * @throws IOException IOException
	 */
	public static byte[] getRequestBytes(HttpServletRequest request) throws IOException {
		int contentLength = request.getContentLength();
		if (contentLength < 0) {
			return null;
		}
		byte[] buffer = new byte[contentLength];
		for (int i = 0; i < contentLength; ) {

			int readlen = request.getInputStream().read(buffer, i, contentLength - i);
			if (readlen == -1) {
				break;
			}
			i += readlen;
		}
		return buffer;
	}

	/**
	 * 获取 request 请求内容
	 *
	 * @param request request
	 * @param buffer buffer
	 * @return String
	 * @throws IOException IOException
	 */
	public static String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
		String charEncoding = request.getCharacterEncoding();
		if (charEncoding == null) {
			charEncoding = StringPool.UTF_8;
		}
		String str = new String(buffer, charEncoding).trim();
		if (Func.isBlank(str)) {
			StringBuilder sb = new StringBuilder();
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String key = parameterNames.nextElement();
				String value = request.getParameter(key);
				StringUtil.appendBuilder(sb, key, "=", value, "&");
			}
			str = StringUtil.removeSuffix(sb.toString(), "&");
		}
		return str.replaceAll("&amp;", "&");
	}
	/**
	 * 去掉指定后缀
	 *
	 * @param str    字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(suffix)) {
			return StringPool.EMPTY;
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return StringUtil.subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

}

