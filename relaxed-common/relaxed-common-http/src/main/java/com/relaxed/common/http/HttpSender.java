package com.relaxed.common.http;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.relaxed.common.core.util.SpringUtils;

import com.relaxed.common.http.core.ISender;
import com.relaxed.common.http.core.request.IRequest;
import com.relaxed.common.http.core.response.IResponse;
import com.relaxed.common.http.domain.HttpResponseWrapper;

import com.relaxed.common.http.domain.IHttpResponse;
import com.relaxed.common.http.domain.RequestForm;

import com.relaxed.common.http.domain.UploadFile;
import com.relaxed.common.http.event.ReqReceiveEvent;
import com.relaxed.common.http.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

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

	private final String baseUrl;

	private final RequestHeaderGenerate headerGenerate;

	public HttpSender(String baseUrl) {
		this.baseUrl = baseUrl;
		this.headerGenerate = () -> null;
	}

	public HttpSender(String baseUrl, RequestHeaderGenerate headerGenerate) {
		this.baseUrl = baseUrl;
		this.headerGenerate = headerGenerate;
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
			log.debug("请求渠道:{} url:{} 参数:{} 响应:{}", channel, requestUrl, requestForm, responseWrapper);
			response = request.convertToResponse(responseWrapper);
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
		Map<String, String> headMap = headerGenerate.generate();
		fillHttpRequestHeader(httpRequest, headMap);
		HttpResponse httpResponse = httpRequest.execute();
		if (httpResponse.getStatus() != 200) {
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
	 * @param response
	 * @param throwable
	 * @param startTime
	 * @param endTime
	 */
	private <R extends IResponse> void publishReqResEvent(String channel, String url, IRequest<R> request,
			RequestForm requestForm, R response, Throwable throwable, Long startTime, Long endTime) {
		ReqReceiveEvent event = new ReqReceiveEvent(channel, url, request, requestForm, response, throwable, startTime,
				endTime);
		SpringUtils.publishEvent(event);
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
				List<UploadFile> files = requestForm.getFiles();
				Optional.ofNullable(files).ifPresent(uploadFiles -> {
					for (UploadFile uploadFile : uploadFiles) {
						httpRequest.form(uploadFile.getFileName(), uploadFile.getFileData());
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
	protected RequestHeaderGenerate headerGenerate() {
		return headerGenerate;
	}

	public interface RequestHeaderGenerate {

		Map<String, String> generate();

	}

}
