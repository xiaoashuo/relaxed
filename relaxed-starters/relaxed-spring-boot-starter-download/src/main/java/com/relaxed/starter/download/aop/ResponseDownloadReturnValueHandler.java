package com.relaxed.starter.download.aop;

import com.relaxed.starter.download.annotation.ResponseDownload;
import com.relaxed.starter.download.handler.DownloadHandler;
import com.relaxed.starter.download.handler.DownloadHandlerChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Yakir
 * @Topic ResponseDownloadReturnValueHandler
 * @Description
 * @date 2022/2/18 10:15
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseDownloadReturnValueHandler implements HandlerMethodReturnValueHandler {

	private final DownloadHandlerChain downloadHandlerChain;

	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return parameter.getMethodAnnotation(ResponseDownload.class) != null;
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest nativeWebRequest) throws Exception {
		/* check */
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		ResponseDownload responseDownload = parameter.getMethodAnnotation(ResponseDownload.class);
		Assert.state(responseDownload != null, "No @Download");
		mavContainer.setRequestHandled(true);
		downloadHandlerChain.process(returnValue, response, responseDownload);

	}

}
