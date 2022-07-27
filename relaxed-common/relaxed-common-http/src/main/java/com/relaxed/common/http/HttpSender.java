package com.relaxed.common.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReferenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.relaxed.common.core.util.SpringUtils;

import com.relaxed.common.http.core.ISender;
import com.relaxed.common.http.core.interceptor.RequestInterceptor;
import com.relaxed.common.http.core.notify.RequestResultNotifier;

import com.relaxed.common.http.core.provider.RequestHeaderProvider;
import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.resource.Resource;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.*;

import com.relaxed.common.http.event.ReqReceiveEvent;
import com.relaxed.common.http.exception.RequestException;
import com.relaxed.common.http.util.GenericTypeUtils;
import com.relaxed.common.model.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * @author Yakir
 * @Topic DefaultSender
 * @Description
 * @date 2022/5/18 8:34
 * @Version 1.0
 */
@Slf4j
public class HttpSender implements ISender<HttpRequest, HttpResponse> {

	/**
	 * 基础url
	 */
	private final String baseUrl;

	/**
	 * 请求头提供者
	 */
	private final RequestHeaderProvider requestHeaderProvider;

	/**
	 * 请求结果通知器
	 */
	private final RequestResultNotifier requestResultNotifier;

	/**
	 * 请求响应拦截器
	 */
	private final RequestInterceptor<HttpRequest, HttpResponse> requestInterceptor;

	private static RequestInterceptor<HttpRequest, HttpResponse> DEFAULT_REQUEST_INTERCEPTOR = new RequestInterceptor<HttpRequest, HttpResponse>() {
		@Override
		public HttpRequest requestInterceptor(HttpRequest request, RequestForm requestForm,
				Map<String, Object> context) {
			return request;
		}

		@Override
		public HttpResponse responseInterceptor(HttpRequest request, HttpResponse response,
				Map<String, Object> context) {
			return response;
		}
	};

	public HttpSender(String baseUrl) {
		this(baseUrl, (url, requestForm) -> null);
	}

	public HttpSender(String baseUrl, RequestHeaderProvider requestHeaderProvider) {
		this(baseUrl, requestHeaderProvider, event -> SpringUtils.getContext().publishEvent(event),
				DEFAULT_REQUEST_INTERCEPTOR);
	}

	public HttpSender(String baseUrl, RequestHeaderProvider requestHeaderProvider,
			RequestResultNotifier requestResultNotifier,
			RequestInterceptor<HttpRequest, HttpResponse> requestInterceptor) {
		this.baseUrl = baseUrl;
		this.requestHeaderProvider = requestHeaderProvider;
		this.requestResultNotifier = requestResultNotifier;
		this.requestInterceptor = requestInterceptor;
	}

	@Override
	public <R extends IResponse> R send(IRequest<R> request, Map<String, String> headerMap) {
		String requestUrl = request.getUrl(baseUrl);
		String channel = request.getChannel();
		RequestForm requestForm = request.generateRequestParam();
		Long startTime = System.currentTimeMillis();
		Map<String, Object> context = new HashMap<>(8);
		R response = null;
		Throwable myThrowable = null;
		try {

			IHttpResponse responseWrapper = doExecute(requestUrl, requestForm, headerMap, context);
			response = request.convertToResponse(responseWrapper);
			return response;
		}
		catch (Throwable throwable) {
			myThrowable = ExceptionUtil.unwrap(throwable);
			throw new RequestException(myThrowable.getMessage(), myThrowable);
		}
		finally {
			log.debug("请求渠道:{} url:{} 参数:{} 响应:{}", channel, requestUrl, requestForm, response);
			// 结束时间
			Long endTime = System.currentTimeMillis();
			publishReqResEvent(channel, requestUrl, request, requestForm, context, response, myThrowable, startTime,
					endTime);
		}
	}

	/**
	 * 真正执行请求方法
	 * @author yakir
	 * @date 2022/5/18 17:52
	 * @param requestUrl
	 * @param requestForm
	 * @param headerMap
	 * @return T
	 */
	protected <T extends IHttpResponse> T doExecute(String requestUrl, RequestForm requestForm,
			Map<String, String> headerMap, Map<String, Object> context) {
		HttpRequest httpRequest = buildHttpRequest(requestUrl, requestForm);
		// 获取用户定义的请求头 全局+局部
		Map<String, String> requestHeadMap = Optional
				.ofNullable(requestHeaderProvider.generate(requestUrl, requestForm)).orElseGet(HashMap::new);
		if (headerMap != null) {
			requestHeadMap.putAll(headerMap);
		}
		// 填充请求头
		fillHttpRequestHeader(httpRequest, requestHeadMap);
		HttpResponse httpResponse = requestInterceptor.responseInterceptor(httpRequest,
				requestInterceptor.requestInterceptor(httpRequest, requestForm, context).execute(), context);
		if (httpResponse.getStatus() != HttpStatus.HTTP_OK) {
			throw new HttpException("{}", httpResponse.body());
		}
		return convertOriginalResponse(httpResponse);
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
	 * @param context
	 * @param response
	 * @param throwable
	 * @param startTime
	 * @param endTime
	 */
	protected <R extends IResponse> void publishReqResEvent(String channel, String url, IRequest<R> request,
			RequestForm requestForm, Map<String, Object> context, R response, Throwable throwable, Long startTime,
			Long endTime) {
		ReqReceiveEvent event = new ReqReceiveEvent(channel, url, request, requestForm, context, response, throwable,
				startTime, endTime);
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
	 * 获取请求基础地址
	 * @author yakir
	 * @date 2022/6/27 13:33
	 * @return java.lang.String
	 */
	protected String getBaseUrl() {
		return baseUrl;
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
	 * 请求结果通知者
	 * @author yakir
	 * @date 2022/5/23 11:06
	 * @return com.relaxed.common.http.core.notify.RequestResultNotifier
	 */
	public RequestResultNotifier getRequestResultNotifier() {
		return requestResultNotifier;
	}

}
