package com.relaxed.common.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.multipart.UploadFile;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.relaxed.common.core.util.SpringUtils;

import com.relaxed.common.http.core.ISender;
import com.relaxed.common.http.core.notify.RequestResultNotifier;
import com.relaxed.common.http.core.provider.RequestConfigProvider;
import com.relaxed.common.http.core.provider.RequestHeaderProvider;
import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.resource.Resource;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.*;

import com.relaxed.common.http.event.ReqReceiveEvent;
import com.relaxed.common.http.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Yakir
 * @Topic DefaultSender
 * @Description
 * @date 2022/5/18 8:34
 * @Version 1.0
 */
@Slf4j
public class HttpSender implements ISender {

	/**
	 * 基础url
	 */
	private final String baseUrl;

	/**
	 * 请求头提供者
	 */
	private final RequestHeaderProvider requestHeaderProvider;

	/**
	 * 请求配置提供者
	 */
	private final RequestConfigProvider requestConfigProvider;

	/**
	 * 请求结果通知器
	 */
	private final RequestResultNotifier requestResultNotifier;

	public HttpSender(String baseUrl) {
		this(baseUrl, (url, requestForm) -> null);
	}

	public HttpSender(String baseUrl, RequestHeaderProvider requestHeaderProvider) {
		this(baseUrl, requestHeaderProvider, () -> null);
	}

	public HttpSender(String baseUrl, RequestHeaderProvider requestHeaderProvider,
			RequestConfigProvider requestConfigProvider) {
		this(baseUrl, requestHeaderProvider, requestConfigProvider,
				(reqReceiveEvent) -> SpringUtils.publishEvent(reqReceiveEvent));
	}

	public HttpSender(String baseUrl, RequestHeaderProvider requestHeaderProvider,
			RequestConfigProvider requestConfigProvider, RequestResultNotifier requestResultNotifier) {
		this.baseUrl = baseUrl;
		this.requestHeaderProvider = requestHeaderProvider;
		this.requestConfigProvider = requestConfigProvider;
		this.requestResultNotifier = requestResultNotifier;
	}

	@Override
	public <R extends IResponse> R send(IRequest<R> request) {
		String requestUrl = request.getUrl(baseUrl);
		String channel = request.getChannel();
		RequestForm requestForm = request.generateRequestParam();
		Long startTime = System.currentTimeMillis();
		R response = null;
		Throwable myThrowable = null;
		try {
			IHttpResponse responseWrapper = doExecute(requestUrl, requestForm);
			response = request.convertToResponse(responseWrapper);
			log.debug("请求渠道:{} url:{} 参数:{} 响应:{}", channel, requestUrl, requestForm, response);
			return response;

		}
		catch (Throwable throwable) {
			myThrowable = ExceptionUtil.unwrap(throwable);
			throw new RequestException(myThrowable);
		}
		finally {
			// 结束时间
			Long endTime = System.currentTimeMillis();
			publishReqResEvent(channel, requestUrl, request, requestForm, response, myThrowable, startTime, endTime);
		}
	}

	/**
	 * 真正执行请求方法
	 * @author yakir
	 * @date 2022/5/18 17:52
	 * @param requestUrl
	 * @param requestForm
	 * @return T
	 */
	protected <T extends IHttpResponse> T doExecute(String requestUrl, RequestForm requestForm) {
		HttpRequest httpRequest = buildHttpRequest(requestUrl, requestForm);
		RequestConfig requestConfig = requestConfigProvider.provide();
		fillHttpConfig(httpRequest, requestConfig);
		Map<String, String> headMap = requestHeaderProvider.generate(requestUrl, requestForm);
		fillHttpRequestHeader(httpRequest, headMap);
		HttpResponse httpResponse = httpRequest.execute();
		if (httpResponse.getStatus() != 200) {
			throw new HttpException("{}", httpResponse.body());
		}
		return convertOriginalResponse(httpResponse);
	}

	/**
	 * 填充http基本配置
	 * @author yakir
	 * @date 2022/5/23 10:10
	 * @param httpRequest
	 * @param requestConfig
	 */
	protected void fillHttpConfig(HttpRequest httpRequest, RequestConfig requestConfig) {
		if (requestConfig == null) {
			return;
		}
		httpRequest.setReadTimeout(requestConfig.getReadTimeout());
		httpRequest.setConnectionTimeout(requestConfig.getConnectionTimeout());
	}

	/**
	 * 转换原始响应
	 * @author yakir
	 * @date 2022/5/19 9:12
	 * @param httpResponse
	 * @return T
	 */
	protected <T extends IHttpResponse> T convertOriginalResponse(HttpResponse httpResponse) {
		HttpResponseWrapper responseWrapper = new HttpResponseWrapper();
		responseWrapper.setCharset(httpResponse.charset());
		responseWrapper.setBodyBytes(httpResponse.bodyBytes());
		return (T) responseWrapper;
	}

	/**
	 * 填充请求头
	 * @author yakir
	 * @date 2022/5/19 9:12
	 * @param httpRequest
	 * @param headMap
	 */
	protected void fillHttpRequestHeader(HttpRequest httpRequest, Map<String, String> headMap) {
		if (MapUtil.isEmpty(headMap)) {
			return;
		}
		for (Map.Entry<String, String> entry : headMap.entrySet()) {
			httpRequest.header(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 发布事件
	 * @author yakir
	 * @date 2022/5/19 9:12
	 * @param channel
	 * @param url
	 * @param request
	 * @param requestForm
	 * @param response
	 * @param throwable
	 * @param startTime
	 * @param endTime
	 */
	private <R extends IResponse> void publishReqResEvent(String channel, String url, IRequest<R> request,
			RequestForm requestForm, R response, Throwable throwable, Long startTime, Long endTime) {
		ReqReceiveEvent event = new ReqReceiveEvent(channel, url, request, requestForm, response, throwable, startTime,
				endTime);
		getRequestResultNotifier().notify(event);
	}

	/**
	 * 构建http请求
	 * @author yakir
	 * @date 2022/5/19 9:12
	 * @param requestUrl
	 * @param requestForm
	 * @return cn.hutool.http.HttpRequest
	 */
	protected HttpRequest buildHttpRequest(String requestUrl, RequestForm requestForm) {
		RequestMethod requestMethod = requestForm.getRequestMethod();
		boolean isGet = requestMethod.name().equalsIgnoreCase(RequestMethod.GET.name());
		HttpRequest httpRequest;
		if (isGet) {
			httpRequest = HttpRequest.of(requestUrl);
			httpRequest.setMethod(convertToHuMethod(requestMethod));
			httpRequest.form(requestForm.getForm());
		}
		else {
			httpRequest = HttpRequest.of(requestUrl);
			httpRequest.setMethod(convertToHuMethod(requestMethod));
			String body = requestForm.getBody();
			if (StrUtil.isNotEmpty(body)) {
				// 走json
				httpRequest.body(body);
			}
			else {
				httpRequest.form(requestForm.getForm());
				List<Resource> requestResources = requestForm.getResources();
				Optional.ofNullable(requestResources).ifPresent(resources -> {
					for (Resource resource : resources) {
						httpRequest.form(resource.getName(),
								new HttpResource(new InputStreamResource(resource.getStream(), resource.getFileName()),
										resource.getContentType()));
					}
				});

			}
		}
		return httpRequest;
	}

	/**
	 * 转换方法
	 * @author yakir
	 * @date 2022/5/19 9:13
	 * @param requestMethod
	 * @return cn.hutool.http.Method
	 */
	public Method convertToHuMethod(RequestMethod requestMethod) {

		Method method;
		switch (requestMethod) {
		case GET:
			method = Method.GET;
			break;
		case POST:
			method = Method.POST;
			break;
		case PUT:
			method = Method.PUT;
			break;
		case PATCH:
			method = Method.PATCH;
			break;
		case DELETE:
			method = Method.DELETE;
			break;
		default:
			throw new RuntimeException("method not found");

		}
		return method;

	}

	/**
	 * 获取头生成器
	 * @author yakir
	 * @date 2022/5/19 9:13
	 * @return com.relaxed.common.http.HttpSender.RequestHeaderGenerate
	 */
	protected RequestHeaderProvider getRequestHeaderProvider() {
		return requestHeaderProvider;
	}

	/**
	 * 获取请求配置信息
	 * @author yakir
	 * @date 2022/5/23 10:13
	 * @return com.relaxed.common.http.core.provider.RequestConfigProvider
	 */
	protected RequestConfigProvider getRequestConfigProvider() {
		return requestConfigProvider;
	}

	/**
	 * 请求结果通知者
	 * @author yakir
	 * @date 2022/5/23 11:06
	 * @return com.relaxed.common.http.core.notify.RequestResultNotifier
	 */
	public RequestResultNotifier getRequestResultNotifier() {
		return requestResultNotifier;
	}

}
