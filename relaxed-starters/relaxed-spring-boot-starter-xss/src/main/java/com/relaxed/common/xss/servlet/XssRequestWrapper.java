package com.relaxed.common.xss.servlet;

import com.relaxed.common.xss.toolkit.HtmlKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic WafRequestWrapper
 * @Description
 * @date 2021/6/26 14:22
 * @Version 1.0
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

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

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value instanceof String) {
			HtmlKit.cleanUnSafe((String) value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null) {
			return null;
		}
		return HtmlKit.cleanUnSafe(value);
	}

}
