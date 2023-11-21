package top.kthirty.core.web.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * XSS过滤
 *
 * @author Kthirty
 */
@AllArgsConstructor
public class XssFilter implements Filter {

	private final XssProperties xssProperties;
	private final XssUrlProperties xssUrlProperties;
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	public void init(FilterConfig config) {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String path = ((HttpServletRequest) request).getServletPath();
		if (isSkip(path)) {
			chain.doFilter(request, response);
		} else {
			XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
			chain.doFilter(xssRequest, response);
		}
	}

	private boolean isSkip(String path) {
		return (xssUrlProperties.getExcludePatterns().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path)))
			|| (xssProperties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path)));
	}

	@Override
	public void destroy() {

	}

}
