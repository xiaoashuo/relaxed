package com.relaxed.common.log.access.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.log.access.filter.LogAccessProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic AbstractAccessLogHandler
 * @Description
 * @date 2024/12/24 9:22
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAccessLogHandler<T> implements AccessLogHandler<T> {

	protected String getRequestBody(HttpServletRequest request, String matchingPattern) {
		String body = null;
		if (!request.getMethod().equals(HttpMethod.GET.name())) {
			try {
				BufferedReader reader = request.getReader();
				if (reader != null) {
					body = (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
				}
				if (StringUtils.hasText(body) && StringUtils.hasText(matchingPattern)) {
					JSONObject reqJsonObj = JSONUtil.parseObj(body);
					reqJsonObj.putByPath(matchingPattern, "");
					body = reqJsonObj.toJSONString(0);
				}

			}
			catch (Exception var3) {
				log.error("读取请求体异常：", var3);
			}
		}

		return body;
	}

	protected String getHeader(HttpServletRequest request) {
		return getHeader(request, headerName -> true);
	}

	protected interface ReqHeaderFilter {

		/**
		 * 过滤请求头
		 * @param headerName
		 * @return true 提取 false 跳过
		 */
		boolean filter(String headerName);

	}

	protected String getHeader(HttpServletRequest request, ReqHeaderFilter reqHeaderFilter) {
		StringBuilder header = new StringBuilder();
		header.append("{");
		Enumeration<String> e = request.getHeaderNames();
		if (e != null) {
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				if (!reqHeaderFilter.filter(name)) {
					continue;
				}
				String value = request.getHeader(name);
				header.append(name).append("=").append("[").append(value).append("]").append(", ");
			}
			header = new StringBuilder(header.substring(0, header.length() - 2));
		}
		header.append("}");
		return header.toString();
	}

	/**
	 * 获取响应体 防止在 {@link RequestContextHolder} 设置内容之前或清空内容之后使用，从而导致获取不到响应体的问题
	 * @param request 请求信息
	 * @param response 响应信息
	 * @return responseBody 响应体
	 */
	protected String getResponseBody(HttpServletRequest request, HttpServletResponse response,
			String matchResponseKey) {
		try {
			if (response instanceof ContentCachingResponseWrapper) {
				ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
				// 获取响应体
				byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
				String responseText = new String(contentAsByteArray, StandardCharsets.UTF_8);
				if (JSONUtil.isTypeJSONObject(responseText) && StrUtil.isNotBlank(matchResponseKey)) {
					JSONObject resJsonObj = JSONUtil.parseObj(responseText);
					resJsonObj.putByPath(matchResponseKey, "");
					responseText = resJsonObj.toJSONString(0);
				}
				return responseText;
			}
			log.warn("对于未包装的响应体，默认不进行读取请求体，请求 uri: [{}]", request.getRequestURI());
		}
		catch (Exception exception) {
			log.error("获取响应体信息失败，请求 uri: [{}]", request.getRequestURI());
		}
		return "";
	}

	/**
	 * 获取参数信息
	 * @param request 请求信息
	 * @return 请求参数
	 */
	protected String getParams(HttpServletRequest request, String matchKey) {
		String params;
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			if (StringUtils.hasText(matchKey)) {
				parameterMap.put(matchKey, new String[0]);
			}
			params = JSONUtil.toJsonStr(parameterMap);
		}
		catch (Exception e) {
			params = "记录参数异常";
			log.error("[prodLog]，参数获取序列化异常", e);
		}
		return params;
	}

}
