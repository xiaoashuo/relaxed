package com.relaxed.common.xss.servlet;

import com.relaxed.common.xss.toolkit.HtmlKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XSS请求包装器 继承自HttpServletRequestWrapper，用于包装HTTP请求 对请求参数进行XSS过滤，包括参数、属性、头部等
 *
 * @author Yakir
 * @since 1.0
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 构造函数
	 * @param request 原始HTTP请求
	 */
	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 获取所有参数，并对参数值进行XSS过滤
	 * @return 过滤后的参数映射
	 */
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = HtmlKit.cleanUnSafe(values[i]);
			}
			map.put(entry.getKey(), values);
		}
		return map;
	}

	/**
	 * 获取指定名称的参数值数组，并对值进行XSS过滤
	 * @param name 参数名称
	 * @return 过滤后的参数值数组
	 */
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = HtmlKit.cleanUnSafe(values[i]);
		}
		return encodedValues;
	}

	/**
	 * 获取指定名称的参数值，并进行XSS过滤
	 * @param name 参数名称
	 * @return 过滤后的参数值
	 */
	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

	/**
	 * 获取指定名称的属性值，如果属性值是字符串则进行XSS过滤
	 * @param name 属性名称
	 * @return 过滤后的属性值
	 */
	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value instanceof String) {
			HtmlKit.cleanUnSafe((String) value);
		}
		return value;
	}

	/**
	 * 获取指定名称的请求头值，并进行XSS过滤
	 * @param name 请求头名称
	 * @return 过滤后的请求头值
	 */
	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

	/**
	 * 获取查询字符串，并进行XSS过滤
	 * @return 过滤后的查询字符串
	 */
	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

}
