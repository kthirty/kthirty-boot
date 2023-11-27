package top.kthirty.core.secure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import top.kthirty.core.boot.secure.SecureUtil;
import top.kthirty.core.secure.annotation.IgnoreSecure;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.api.R;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.tool.jackson.JsonUtil;
import top.kthirty.core.tool.utils.Charsets;
import top.kthirty.core.web.utils.RequestMappingHolder;

import java.io.IOException;
import java.util.Objects;

/**
 * jwt拦截器校验
 *
 * @author Kthirty
 */
@Slf4j
@AllArgsConstructor
public class SecureInterceptor implements AsyncHandlerInterceptor {
	private static final String CONTENT_TYPE_NAME = "Content-type";

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) {
		IgnoreSecure annotation = RequestMappingHolder.getAnnotation(request, IgnoreSecure.class);
		if(Func.notNull(annotation) && annotation.request()){
			return true;
		}
		if (null != SecureUtil.getCurrentUser()) {
			return true;
		} else {
			R result = R.fail(SystemResultCode.NOT_LOGIN);
			response.setCharacterEncoding(Charsets.UTF_8_NAME);
			response.setHeader(CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
			} catch (IOException ignore) { }
			return false;
		}
	}

}
