package top.kthirty.core.web.error;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.api.R;
import top.kthirty.core.tool.api.SystemResultCode;
import top.kthirty.core.web.utils.WebUtil;

import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author Kthirty
 */
@Slf4j
@AllArgsConstructor
public class KthirtyErrorAttributes extends DefaultErrorAttributes {
	private final List<ErrorHandler> errorHandlers;

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//		String requestUri = this.getAttr(webRequest, "javax.servlet.error.request_uri");
		String status = this.getAttr(webRequest, "javax.servlet.error.status_code");
		Throwable error = getError(webRequest);
		R result;
		if (error == null) {
//			log.error("URL:{} error status:{}", requestUri, status);
			result = R.fail(SystemResultCode.FAILURE, "系统未知异常[HttpStatus]:" + status);
		} else {
			for (ErrorHandler errorHandler : errorHandlers) {
				if(errorHandler.support(error)){
					return BeanUtil.toMap(errorHandler.handle(error, WebUtil.getResponse()));
				}
			}
			log.error("未处理的异常",error);
			result = R.fail(SystemResultCode.FAILURE);
		}
		return BeanUtil.toMap(result);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private <T> T getAttr(WebRequest webRequest, String name) {
		return (T) webRequest.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

}
