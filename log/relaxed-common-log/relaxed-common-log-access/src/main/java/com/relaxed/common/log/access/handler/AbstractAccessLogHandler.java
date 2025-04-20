package com.relaxed.common.log.access.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.core.request.RepeatBodyRequestWrapper;
import com.relaxed.common.log.access.filter.LogAccessProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 访问日志处理器抽象类。 提供了访问日志处理的基础实现。 主要功能包括： 1. 获取请求头信息 2. 获取请求参数 3. 获取请求体 4. 获取响应体 5. 支持字段过滤和替换
 *
 * @param <T> 请求实体类型
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public abstract class AbstractAccessLogHandler<T> implements AccessLogHandler<T> {

	/**
	 * 获取所有请求头信息
	 * @param request HTTP请求
	 * @return 请求头信息JSON字符串
	 */
	protected String getHeader(HttpServletRequest request) {
		return getHeader(request, headerName -> true);
	}

	/**
	 * 请求头过滤器接口
	 */
	protected interface ReqHeaderFilter {

		/**
		 * 过滤请求头
		 * @param headerName 请求头名称
		 * @return true 表示需要提取，false 表示跳过
		 */
		boolean filter(String headerName);

	}

	/**
	 * 根据过滤器提取请求头信息
	 * @param request HTTP请求
	 * @param reqHeaderFilter 请求头过滤器
	 * @return 过滤后的请求头信息JSON字符串
	 */
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
	 * 获取指定请求头信息
	 * @param request HTTP请求
	 * @param headerNames 需要获取的请求头名称数组
	 * @return 指定请求头信息JSON字符串
	 */
	protected String getHeader(HttpServletRequest request, String... headerNames) {
		Map<String, Integer> headMap = new HashMap<>();
		for (String headerName : headerNames) {
			headMap.put(headerName, 1);
		}
		return getHeader(request, headerName -> {
			if (headMap.get(headerName) != null) {
				return true;
			}
			return false;
		});
	}

	/**
	 * 获取响应体信息
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param matchResponseKey 需要过滤的响应字段
	 * @return 响应体信息
	 */
	protected String getResponseBody(HttpServletRequest request, HttpServletResponse response,
			String matchResponseKey) {
		return getResponseBody(request, response, matchResponseKey, "");
	}

	/**
	 * 获取响应体信息 防止在 {@link RequestContextHolder} 设置内容之前或清空内容之后使用，从而导致获取不到响应体的问题
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param matchResponseKey 需要过滤的响应字段
	 * @param replaceText 替换文本
	 * @return 响应体信息
	 */
	protected String getResponseBody(HttpServletRequest request, HttpServletResponse response, String matchResponseKey,
			String replaceText) {
		try {
			if (response instanceof ContentCachingResponseWrapper) {
				ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
				// 获取响应体
				byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
				String responseText = new String(contentAsByteArray, StandardCharsets.UTF_8);
				if (JSONUtil.isTypeJSONObject(responseText) && StrUtil.isNotBlank(matchResponseKey)) {
					JSONObject resJsonObj = JSONUtil.parseObj(responseText);
					List<String> matchkeys = StrUtil.split(matchResponseKey, ",");
					for (String matchKey : matchkeys) {
						Object val = resJsonObj.getByPath(matchKey);
						if (ObjectUtil.isNotEmpty(val)) {
							resJsonObj.putByPath(matchKey, replaceText);
						}
					}

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
	 * 获取请求参数
	 * @param request HTTP请求
	 * @param matchKey 需要过滤的参数字段
	 * @return 请求参数JSON字符串
	 */
	protected String getParams(HttpServletRequest request, String matchKey) {
		return getParams(request, matchKey, "");
	}

	/**
	 * 获取请求参数
	 * @param request HTTP请求
	 * @param matchFieldKey 需要过滤的参数字段
	 * @param replaceText 替换文本
	 * @return 请求参数JSON字符串
	 */
	protected String getParams(HttpServletRequest request, String matchFieldKey, String replaceText) {
		String params;
		try {
			// 防止改变原请求参数
			Map<String, String[]> wrapperParterMap = new HashMap<>(request.getParameterMap());
			if (StringUtils.hasText(matchFieldKey)) {
				List<String> matchKeys = StrUtil.split(matchFieldKey, ",");
				for (String matchKey : matchKeys) {
					String[] vals = wrapperParterMap.get(matchKey);
					if (ArrayUtil.isNotEmpty(vals)) {
						wrapperParterMap.put(matchKey, new String[] { replaceText });
					}
				}

			}
			params = JSONUtil.toJsonStr(wrapperParterMap);
		}
		catch (Exception e) {
			params = "记录参数异常";
			log.error("[prodLog]，参数获取序列化异常", e);
		}
		return params;
	}

	/**
	 * 获取请求体
	 * @param request HTTP请求
	 * @param matchingPattern 需要过滤的字段
	 * @return 请求体内容
	 */
	protected String getRequestBody(HttpServletRequest request, String matchingPattern) {
		return getRequestBody(request, matchingPattern, "");
	}

	/**
	 * 获取请求体
	 * @param request HTTP请求
	 * @param matchingFieldKey 需要过滤的字段
	 * @param replaceText 替换文本
	 * @return 请求体内容
	 */
	protected String getRequestBody(HttpServletRequest request, String matchingFieldKey, String replaceText) {
		RepeatBodyRequestWrapper wrapperRequest = WebUtils.getNativeRequest(request, RepeatBodyRequestWrapper.class);
		if (wrapperRequest == null) {
			return null;
		}
		if (request.getMethod().equals(HttpMethod.GET.name())) {
			return null;
		}
		String body = getRequestBodyText(wrapperRequest);
		if (StringUtils.hasText(body) && StringUtils.hasText(matchingFieldKey)) {
			JSONObject reqJsonObj = JSONUtil.parseObj(body);
			List<String> matchkeys = StrUtil.split(matchingFieldKey, ",");
			for (String matchkey : matchkeys) {
				Object val = reqJsonObj.getByPath(matchkey);
				if (ObjectUtil.isNotEmpty(val)) {
					reqJsonObj.putByPath(matchkey, replaceText);
				}

			}
			// 改变后值赋给body
			body = reqJsonObj.toJSONString(0);
		}
		return body;
	}

	/**
	 * 获取请求体文本
	 * @param wrapperRequest 包装后的请求
	 * @return 请求体文本
	 */
	protected String getRequestBodyText(RepeatBodyRequestWrapper wrapperRequest) {
		String body;

		if (wrapperRequest.getCharacterEncoding() != null) {
			body = getMessagePayload(wrapperRequest.getBodyByteArray(), -1, wrapperRequest.getCharacterEncoding());
		}
		else {
			body = getMessagePayload(wrapperRequest.getBodyByteArray(), -1, Charset.defaultCharset().name());
		}
		return body;
	}

	/**
	 * 获取消息负载
	 * @param buf 字节数组
	 * @param maxLength 最大长度
	 * @param characterEncoding 字符编码
	 * @return 消息文本
	 */
	protected String getMessagePayload(byte[] buf, int maxLength, String characterEncoding) {
		if (buf.length > 0) {
			try {
				if (maxLength < 0) {
					return new String(buf, characterEncoding);
				}
				else if (maxLength == 0) {
					return "";
				}
				else {
					return new String(buf, 0, Math.min(buf.length, maxLength), characterEncoding);
				}
			}
			catch (UnsupportedEncodingException ex) {
				return "[unknown]";
			}
		}
		return null;
	}

}