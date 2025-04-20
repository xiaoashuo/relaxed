package com.relaxed.common.core.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * HTTP请求参数映射修改包装器。 该类继承自 {@link HttpServletRequestWrapper}，用于在不修改原始请求对象的情况下，
 * 修改或替换请求参数映射。这在需要动态修改请求参数时特别有用。
 *
 * <p>
 * 使用示例： <pre>{@code
 * // 创建新的参数映射
 * Map<String, String[]> newParams = new HashMap<>();
 * newParams.put("modifiedParam", new String[]{"newValue"});
 *
 * // 创建包装器
 * HttpServletRequest wrappedRequest = new ModifyParamMapRequestWrapper(originalRequest, newParams);
 * }</pre>
 *
 * @author Hccake
 * @since 1.0.0
 */
public class ModifyParamMapRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 修改后的参数映射
	 */
	private final Map<String, String[]> parameterMap;

	/**
	 * 构造一个新的请求包装器
	 * @param request 原始的HTTP请求对象
	 * @param parameterMap 新的参数映射，用于替换原始请求中的参数映射
	 */
	public ModifyParamMapRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap) {
		super(request);
		this.parameterMap = parameterMap;
	}

	/**
	 * 获取修改后的参数映射 此方法覆盖父类的 {@link HttpServletRequestWrapper#getParameterMap()} 方法，
	 * 返回在构造时提供的新参数映射
	 * @return 修改后的参数映射
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

}
