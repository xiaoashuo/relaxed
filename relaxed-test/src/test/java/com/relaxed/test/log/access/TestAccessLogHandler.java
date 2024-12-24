package com.relaxed.test.log.access;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.URLUtil;
import com.relaxed.common.core.util.IpUtils;
import com.relaxed.common.log.access.filter.AccessLogFilter;
import com.relaxed.common.log.access.filter.LogAccessProperties;
import com.relaxed.common.log.access.filter.LogAccessRule;
import com.relaxed.common.log.access.handler.AbstractAccessLogHandler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic DefaultAccessLogFilter
 * @Description
 * @date 2024/12/23 18:27
 * @Version 1.0
 */

@Slf4j
public class TestAccessLogHandler extends AbstractAccessLogHandler<AccessLog> {

	public static final String TRACE_ID = "traceId";

	@Override
	public AccessLog beforeRequest(HttpServletRequest request, LogAccessRule logAccessRule) {
		AccessLog paramMap = new AccessLog();
		Object matchingPatternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String matchingPattern = matchingPatternAttr == null ? "" : String.valueOf(matchingPatternAttr);
		String uri = URLUtil.getPath(request.getRequestURI());
		paramMap.setTraceId(MDC.get(TRACE_ID));
		paramMap.setUri(uri);
		paramMap.setMethod(request.getMethod());
		paramMap.setIp(IpUtils.getIpAddr(request));
		paramMap.setMatchingPattern(matchingPattern);
		paramMap.setUserAgent(request.getHeader("user-agent"));
		paramMap.setCreatedTime(LocalDateTime.now());
		return paramMap;
	}

	@Override
	public void afterRequest(AccessLog buildParam, HttpServletRequest request, HttpServletResponse response,
			Long executionTime, Throwable myThrowable, LogAccessRule logAccessRule) {
		buildParam.setUpdatedTime(LocalDateTime.now());
		buildParam.setTime(executionTime);
		buildParam.setHttpStatus(response.getStatus());
		buildParam.setErrorMsg(Optional.ofNullable(myThrowable).map(Throwable::getMessage).orElse(""));
		LogAccessRule.RecordOption recordOption = logAccessRule.getRecordOption();
		LogAccessRule.FieldFilter fieldFilter = logAccessRule.getFieldFilter();

		// 记录请求
		if (recordOption.isIncludeRequest()) {
			String matchRequestKey = fieldFilter.getMatchRequestKey();
			// 获取普通参数
			String params = getParams(request, matchRequestKey);
			buildParam.setReqParams(params);
			// 非文件上传请求，记录body，用户改密时不记录body
			// TODO 使用注解控制此次请求是否记录body，更方便个性化定制
			if (!isMultipartContent(request)) {
				buildParam.setReqBody(getRequestBody(request, matchRequestKey));

			}
		}
		// 记录响应
		if (recordOption.isIncludeResponse()) {
			String matchResponseKey = fieldFilter.getMatchResponseKey();
			buildParam.setResult(getResponseBody(request, response, matchResponseKey));
		}

		String header = getHeader(request, new ReqHeaderFilter() {
			@Override
			public boolean filter(String headerName) {
				if ("Accept".equals(headerName) || "Content-Type".equals(headerName)) {
					return true;
				}
				return false;
			}
		});
		log.info("\n请求头:\n{}\n请求记录:\n{}", header, convertToAccessLogStr(buildParam));

	}

	/**
	 * 判断是否是multipart/form-data请求
	 * @param request 请求信息
	 * @return 是否是multipart/form-data请求
	 */
	public boolean isMultipartContent(HttpServletRequest request) {
		// 获取Content-Type
		String contentType = request.getContentType();
		return (contentType != null) && (contentType.toLowerCase().startsWith("multipart/"));
	}

	public String convertToAccessLogStr(AccessLog accessLog) {
		String LINE_SEPARATOR = System.lineSeparator();
		StringBuilder reqInfo = new StringBuilder().append("traceId:").append(accessLog.getTraceId())
				.append(LINE_SEPARATOR).append("userId:").append(accessLog.getUserId()).append(LINE_SEPARATOR)
				.append("userName:").append(accessLog.getUsername()).append(LINE_SEPARATOR).append("uri:")
				.append(accessLog.getUri()).append(LINE_SEPARATOR).append("matchingPattern:")
				.append(accessLog.getMatchingPattern()).append(LINE_SEPARATOR).append("method:")
				.append(accessLog.getMethod()).append(LINE_SEPARATOR).append("userAgent:")
				.append(accessLog.getUserAgent()).append(LINE_SEPARATOR).append("reqParams:")
				.append(accessLog.getReqParams()).append(LINE_SEPARATOR).append("reqBody:")
				.append(accessLog.getReqBody()).append(LINE_SEPARATOR).append("httpStatus:")
				.append(accessLog.getHttpStatus()).append(LINE_SEPARATOR).append("result:")
				.append(accessLog.getResult()).append(LINE_SEPARATOR).append("errorMsg:")
				.append(accessLog.getErrorMsg()).append(LINE_SEPARATOR).append("time:").append(accessLog.getTime())
				.append(LINE_SEPARATOR).append("createdTime:").append(accessLog.getCreatedTime())
				.append(LINE_SEPARATOR);
		return reqInfo.toString();
	}

}
