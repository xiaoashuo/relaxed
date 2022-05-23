package com.relaxed.common.http.test.custom;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.relaxed.common.http.HttpSender;
import com.relaxed.common.http.core.header.RequestHeaderGenerate;
import com.relaxed.common.http.domain.HttpResponseWrapper;
import com.relaxed.common.http.domain.IHttpResponse;
import com.relaxed.common.http.domain.RequestForm;

import java.util.Map;

/**
 * @author Yakir
 * @Topic CustomSender
 * @Description
 * @date 2022/5/18 17:55
 * @Version 1.0
 */
public class CustomSender extends HttpSender {

	public CustomSender(String baseUrl) {
		super(baseUrl);

	}

	public CustomSender(String baseUrl, RequestHeaderGenerate headerGenerate) {
		super(baseUrl, headerGenerate);
	}

	/**
	 * 此方法 可以构建自己的http client
	 * @author yakir
	 * @date 2022/5/18 18:02
	 * @param requestUrl
	 * @param requestForm
	 * @return T
	 */
	@Override
	protected <T extends IHttpResponse> T doExecute(String requestUrl, RequestForm requestForm) {
		HttpRequest httpRequest = buildHttpRequest(requestUrl, requestForm);
		Map<String, String> headMap = super.headerGenerate().generate(requestUrl, requestForm);
		fillHttpRequestHeader(httpRequest, headMap);
		HttpResponse httpResponse = httpRequest.execute();
		if (httpResponse.getStatus() != 200) {
			throw new HttpException("request failed -{}", httpResponse.body());
		}
		CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper();
		responseWrapper.setCharset(httpResponse.charset());
		responseWrapper.setBodyBytes(httpResponse.bodyBytes());
		return (T) responseWrapper;
	}

}
