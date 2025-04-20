package com.relaxed.common.core.util;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Web 应用工具类，提供 HTTP 请求、响应和会话相关的实用方法。 继承自 Spring 的 WebUtils，扩展了更多便捷的操作方法，包括： 1.
 * 请求参数获取和类型转换 2. Cookie 操作 3. 响应内容渲染 4. Session 管理 5. Header 处理
 *
 * @author ruoyi
 * @since 1.0
 */
@Slf4j
public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 获取请求参数的字符串值
	 * @param name 参数名
	 * @return 参数值，如果参数不存在则返回 null
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 获取请求参数的字符串值，支持默认值
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数值，如果参数不存在则返回默认值
	 */
	public static String getParameter(String name, String defaultValue) {
		return Convert.toStr(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获取请求参数并转换为整数
	 * @param name 参数名
	 * @return 参数的整数值，如果转换失败则返回 null
	 */
	public static Integer getParameterToInt(String name) {
		return Convert.toInt(getRequest().getParameter(name));
	}

	/**
	 * 获取请求参数并转换为整数，支持默认值
	 * @param name 参数名
	 * @param defaultValue 默认值
	 * @return 参数的整数值，如果转换失败则返回默认值
	 */
	public static Integer getParameterToInt(String name, Integer defaultValue) {
		return Convert.toInt(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获取当前请求的 HttpServletRequest 对象
	 * @return 当前请求的 HttpServletRequest 对象
	 */
	public static HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获取当前请求的 HttpServletResponse 对象
	 * @return 当前请求的 HttpServletResponse 对象
	 */
	public static HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	/**
	 * 获取指定请求头的值
	 * @param name 请求头名称
	 * @return 请求头的值，如果不存在则返回 null
	 */
	public static String getHeaderValue(String name) {
		return getRequestAttributes().getRequest().getHeader(name);
	}

	/**
	 * 获取当前会话对象
	 * @return 当前会话对象，如果不存在则创建新的会话
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 获取当前请求的 ServletRequestAttributes 对象
	 * @return 当前请求的 ServletRequestAttributes 对象
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return (ServletRequestAttributes) attributes;
	}

	/**
	 * 读取指定名称的 Cookie 值
	 * @param name Cookie 名称
	 * @return Cookie 值，如果不存在则返回 null
	 */
	public String getCookieVal(String name) {
		HttpServletRequest request = getRequest();
		Assert.notNull(request, "request from RequestContextHolder is null");
		return getCookieVal(request, name);
	}

	/**
	 * 从指定请求中读取指定名称的 Cookie 值
	 * @param request HTTP 请求对象
	 * @param name Cookie 名称
	 * @return Cookie 值，如果不存在则返回 null
	 */
	public String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除指定名称的 Cookie
	 * @param response HTTP 响应对象
	 * @param key Cookie 名称
	 */
	public void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置 Cookie
	 * @param response HTTP 响应对象
	 * @param name Cookie 名称
	 * @param value Cookie 值
	 * @param maxAgeInSeconds Cookie 最大存活时间（秒）
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 将字符串渲染到客户端，使用 JSON 格式
	 * @param response HTTP 响应对象
	 * @param result 要渲染的字符串内容
	 */
	public static void renderString(HttpServletResponse response, String result) {
		renderString(response, result, "application/json");
	}

	/**
	 * 将字符串渲染到客户端，使用指定的内容类型
	 * @param response HTTP 响应对象
	 * @param result 要渲染的字符串内容
	 * @param contentType 内容类型
	 */
	public static void renderString(HttpServletResponse response, String result, String contentType) {
		renderString(response, result, HttpStatus.OK.value(), contentType);
	}

	/**
	 * 将字符串渲染到客户端，使用指定的状态码和内容类型
	 * @param response HTTP 响应对象
	 * @param result 要渲染的字符串内容
	 * @param status HTTP 状态码
	 * @param contentType 内容类型
	 */
	public static void renderString(HttpServletResponse response, String result, Integer status, String contentType) {
		try {
			response.setStatus(status);
			response.setContentType(contentType);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(result);
		}
		catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
