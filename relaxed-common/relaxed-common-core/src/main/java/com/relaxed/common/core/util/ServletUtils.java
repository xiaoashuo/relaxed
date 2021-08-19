package com.relaxed.common.core.util;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 客户端工具类
 *
 * @author ruoyi
 */
@Slf4j
public class ServletUtils {

	/**
	 * 获取String参数
	 */
	public static String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	/**
	 * 获取String参数
	 */
	public static String getParameter(String name, String defaultValue) {
		return Convert.toStr(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获取Integer参数
	 */
	public static Integer getParameterToInt(String name) {
		return Convert.toInt(getRequest().getParameter(name));
	}

	/**
	 * 获取Integer参数
	 */
	public static Integer getParameterToInt(String name, Integer defaultValue) {
		return Convert.toInt(getRequest().getParameter(name), defaultValue);
	}

	/**
	 * 获取request
	 */
	public static HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获取response
	 */
	public static HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	/**
	 * 根据请求头名称 获取值
	 * @param name
	 * @return
	 */
	public static String getHeaderValue(String name) {
		return getRequestAttributes().getRequest().getHeader(name);
	}

	/**
	 * 获取Session
	 * @author yakir
	 * @date 2021/8/19 11:06
	 * @return javax.servlet.http.HttpSession
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static ServletRequestAttributes getRequestAttributes() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return (ServletRequestAttributes) attributes;
	}

	/**
	 * 读取cookie
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(String name) {
		HttpServletRequest request = getRequest();
		Assert.notNull(request, "request from RequestContextHolder is null");
		return getCookieVal(request, name);
	}

	/**
	 * 读取cookie
	 * @param request HttpServletRequest
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除 某个指定的cookie
	 * @param response HttpServletResponse
	 * @param key cookie key
	 */
	public void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response HttpServletResponse
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds maxage
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 将字符串渲染到客户端
	 * @author yakir
	 * @date 2021/8/19 11:03
	 * @param response
	 * @param result
	 */
	public static void renderString(HttpServletResponse response, String result) {
		renderString(response, result, "application/json");
	}

	/**
	 * 将字符串渲染到客户端
	 * @author yakir
	 * @date 2021/8/19 11:03
	 * @param response
	 * @param result
	 * @param contentType
	 */
	public static void renderString(HttpServletResponse response, String result, String contentType) {
		renderString(response, result, HttpStatus.OK.value(), contentType);
	}

	/**
	 * 将字符串渲染到客户端
	 * @author yakir
	 * @date 2021/8/19 11:05
	 * @param response
	 * @param result
	 * @param status
	 * @param contentType
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
