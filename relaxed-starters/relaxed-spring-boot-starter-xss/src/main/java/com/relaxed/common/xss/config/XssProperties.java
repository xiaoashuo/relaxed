package com.relaxed.common.xss.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yakir
 * @Topic XssProperties
 * @Description
 * @date 2021/6/26 14:45
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = XssProperties.PREFIX)
public class XssProperties {

	public static final String PREFIX = "relaxed.security.xss";

	/**
	 * AntPath规则匹配器
	 */
	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * 是否开启
	 */
	private boolean enabled = true;

	/**
	 * xss 过滤包含的路径（Ant风格）
	 **/
	private Set<String> includePaths = Collections.singleton("/**");

	/**
	 * xss 需要排除的路径（Ant风格），优先级高于包含路径
	 **/
	private Set<String> excludePaths = new HashSet<>();

	/**
	 * 需要处理的 HTTP 请求方法集合
	 */
	private final Set<String> includeHttpMethods = new HashSet<>(
			Arrays.asList(HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.PATCH.name()));

	/**
	 * 判断是否不应该过滤
	 * @param request
	 * @return true 不过滤 false 过滤
	 */
	public boolean shouldNotFilter(HttpServletRequest request) {
		// 关闭直接跳过
		if (!this.isEnabled()) {
			return true;
		}

		// 请求方法检查
		if (!StrUtil.equalsAnyIgnoreCase(request.getMethod(), this.getIncludeHttpMethods().toArray(new String[] {}))) {
			return true;
		}

		// 请求路径检查
		String requestUri = request.getRequestURI();
		// 此路径是否不需要处理
		for (String exclude : this.getExcludePaths()) {
			if (ANT_PATH_MATCHER.match(exclude, requestUri)) {
				return true;
			}
		}

		// 路径是否包含
		for (String include : this.getIncludePaths()) {
			if (ANT_PATH_MATCHER.match(include, requestUri)) {
				return false;
			}
		}

		return false;
	}

}
