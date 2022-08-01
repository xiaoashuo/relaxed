package com.relaxed.common.http.test.ocr;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.relaxed.common.http.HttpSender;
import com.relaxed.common.http.core.interceptor.RequestInterceptor;
import com.relaxed.common.http.core.notify.RequestResultNotifier;
import com.relaxed.common.http.core.provider.RequestHeaderProvider;
import com.relaxed.common.http.domain.RequestForm;
import com.relaxed.common.http.test.example.file.FileRequest;
import com.relaxed.common.http.test.example.file.FileResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic OcrTest
 * @Description
 * @date 2022/6/20 16:24
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("local")
public class OcrTest {

	private final String baseUrl = "https://cvm.tencentcloudapi.com";

	private RequestHeaderProvider requestHeaderProvider = (requestUrl, requestForm) -> getRequestHeader(requestUrl,
			requestForm);

	private Map<String, String> getRequestHeader(String requestUrl, RequestForm requestForm) {
		Map<String, String> map = new HashMap<>();
		map.put("appId", "");
		map.put("channel", "");
		map.put("operatorId", "");
		map.put("operatorName", "");
		return map;
	}

	public HttpSender buildHttpSender() {
		RequestResultNotifier requestResultNotifier = reqReceiveEvent -> log.info("event {}", reqReceiveEvent);

		RequestInterceptor<HttpRequest, HttpResponse> requestInterceptor = new RequestInterceptor<HttpRequest, HttpResponse>() {
			@Override
			public HttpRequest requestInterceptor(HttpRequest request, RequestForm requestForm,
					Map<String, Object> context) {
				request.setConnectionTimeout(10000);
				request.setReadTimeout(10000);
				return request;
			}

			@Override
			public HttpResponse responseInterceptor(HttpRequest request, HttpResponse response,
					Map<String, Object> context) {
				return response;
			}
		};
		HttpSender httpSender = new HttpSender(baseUrl, requestHeaderProvider, requestResultNotifier,
				requestInterceptor);
		return httpSender;
	}

	@Test
	public void testPersonFaceValid() {

		HttpSender httpSender = buildHttpSender();
		FileRequest request = new FileRequest();
		request.setChannelNo("test");
		request.setRequestMethod(RequestMethod.GET);
		request.setFileNo("48");
		log.info("请求参数:{}", request);
		FileResponse response = httpSender.send(request);
		log.info("请求响应:{}", response);
		String fileContent = response.getFileContent();
		File file = new File("test.pdf");
		// FileUtil.writeBytes(Base64.decode(fileContent), file);
	}

}
