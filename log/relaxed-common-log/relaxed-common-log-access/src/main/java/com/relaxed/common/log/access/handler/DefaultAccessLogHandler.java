package com.relaxed.common.log.access.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.URLUtil;
import com.relaxed.common.core.util.IpUtils;
import com.relaxed.common.log.access.filter.LogAccessProperties;

import com.relaxed.common.log.access.filter.LogAccessRule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 默认访问日志处理器实现类。 提供了访问日志的默认实现。 主要功能包括： 1. 记录请求基本信息（URI、方法、IP等） 2. 记录请求参数和请求体 3. 记录响应结果 4.
 * 记录执行时间和异常信息 5. 支持字段过滤和替换
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
public class DefaultAccessLogHandler extends AbstractAccessLogHandler<Map<String, String>> {

	/**
	 * 跟踪ID的MDC键名
	 */
	public static final String TRACE_ID = "traceId";

	/**
	 * 请求前处理，构建请求基本信息
	 * @param request HTTP请求
	 * @param logAccessRule 访问日志规则
	 * @return 请求基本信息Map
	 */
	@Override
	public Map<String, String> beforeRequest(HttpServletRequest request, LogAccessRule logAccessRule) {
		Map<String, String> paramMap = new HashMap<>();
		Object matchingPatternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String matchingPattern = matchingPatternAttr == null ? "" : String.valueOf(matchingPatternAttr);
		String uri = URLUtil.getPath(request.getRequestURI());
		paramMap.put("traceId", MDC.get(TRACE_ID));
		paramMap.put("uri", uri);
		paramMap.put("method", request.getMethod());
		paramMap.put("ip", IpUtils.getIpAddr(request));
		paramMap.put("matchingPattern", matchingPattern);
		paramMap.put("userAgent", request.getHeader("user-agent"));
		paramMap.put("createdTime",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

		return paramMap;
	}

	/**
	 * 请求后处理，记录完整的访问日志
	 * @param buildParam 请求前构建的参数
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param executionTime 请求执行时间（毫秒）
	 * @param myThrowable 请求处理过程中的异常
	 * @param logAccessRule 访问日志规则
	 */
	@Override
	public void afterRequest(Map<String, String> buildParam, HttpServletRequest request, HttpServletResponse response,
			Long executionTime, Throwable myThrowable, LogAccessRule logAccessRule) {
		buildParam.put("updatedTime",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
		buildParam.put("time", executionTime + "");
		buildParam.put("httpStatus", response.getStatus() + "");
		buildParam.put("errorMsg", Optional.ofNullable(myThrowable).map(Throwable::getMessage).orElse(""));
		LogAccessRule.RecordOption recordOption = logAccessRule.getRecordOption();
		LogAccessRule.FieldFilter fieldFilter = logAccessRule.getFieldFilter();

		// 记录请求
		if (recordOption.isIncludeRequest()) {
			String matchRequestKey = fieldFilter.getMatchRequestKey();
			// 获取普通参数
			String params = getParams(request, matchRequestKey, fieldFilter.getReplaceText());
			buildParam.put("reqParams", params);
			// 非文件上传请求，记录body，用户改密时不记录body
			// TODO 使用注解控制此次请求是否记录body，更方便个性化定制
			if (!isMultipartContent(request)) {
				buildParam.put("reqBody", getRequestBody(request, matchRequestKey, fieldFilter.getReplaceText()));

			}
		}
		// 记录响应
		if (recordOption.isIncludeResponse()) {
			String matchResponseKey = fieldFilter.getMatchResponseKey();
			buildParam.put("result",
					getResponseBody(request, response, matchResponseKey, fieldFilter.getReplaceText()));
		}

		String header = getHeader(request);
		this.handleLog(header, buildParam);
	}

	/**
	 * 处理日志记录
	 * @param header 请求头信息
	 * @param buildParam 构建的参数
	 */
	protected void handleLog(String header, Map<String, String> buildParam) {
		log.info("\n请求头:\n{}\n请求记录:\n{}", header, convertToAccessLogStr(buildParam));
	}

	/**
	 * 判断是否是multipart/form-data请求
	 * @param request HTTP请求
	 * @return true 表示是multipart/form-data请求，false 表示不是
	 */
	public boolean isMultipartContent(HttpServletRequest request) {
		// 获取Content-Type
		String contentType = request.getContentType();
		return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
	}

	/**
	 * 将参数Map转换为访问日志字符串
	 * @param params 参数Map
	 * @return 格式化后的访问日志字符串
	 */
	public String convertToAccessLogStr(Map<String, String> params) {
		String LINE_SEPARATOR = System.lineSeparator();
		StringBuilder reqInfo = new StringBuilder().append("traceId:").append(params.get("traceId"))
				.append(LINE_SEPARATOR).append("userId:").append(params.get("userId")).append(LINE_SEPARATOR)
				.append("userName:").append(params.get("userName")).append(LINE_SEPARATOR).append("uri:")
				.append(params.get("uri")).append(LINE_SEPARATOR).append("matchingPattern:")
				.append(params.get("matchingPattern")).append(LINE_SEPARATOR).append("method:")
				.append(params.get("method")).append(LINE_SEPARATOR).append("userAgent:")
				.append(params.get("userAgent")).append(LINE_SEPARATOR).append("reqParams:")
				.append(params.get("reqParams")).append(LINE_SEPARATOR).append("reqBody:").append(params.get("reqBody"))
				.append(LINE_SEPARATOR).append("httpStatus:").append(params.get("httpStatus")).append(LINE_SEPARATOR)
				.append("result:").append(params.get("result")).append(LINE_SEPARATOR).append("errorMsg:")
				.append(params.get("errorMsg")).append(LINE_SEPARATOR).append("time:").append(params.get("time"))
				.append(LINE_SEPARATOR).append("createdTime:").append(params.get("createdTime")).append(LINE_SEPARATOR)
				.append("updatedTime:").append(params.get("updatedTime")).append(LINE_SEPARATOR);
		return reqInfo.toString();
	}

}
