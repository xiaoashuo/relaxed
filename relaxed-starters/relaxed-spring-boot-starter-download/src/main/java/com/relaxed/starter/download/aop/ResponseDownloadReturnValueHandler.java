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
 * 响应下载返回值处理器。 用于处理带有@ResponseDownload注解的方法返回值，实现文件下载功能。
 * 通过DownloadHandlerChain调用相应的下载处理器处理不同类型的文件下载。
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseDownloadReturnValueHandler implements HandlerMethodReturnValueHandler {

	private final DownloadHandlerChain downloadHandlerChain;

	/**
	 * 判断是否支持处理当前返回值类型。 检查方法是否带有@ResponseDownload注解。
	 * @param parameter 方法参数
	 * @return 如果方法带有@ResponseDownload注解则返回true，否则返回false
	 */
	@Override
	public boolean supportsReturnType(MethodParameter parameter) {
		return parameter.getMethodAnnotation(ResponseDownload.class) != null;
	}

	/**
	 * 处理返回值，实现文件下载功能。 获取HttpServletResponse和@ResponseDownload注解配置，
	 * 通过DownloadHandlerChain处理文件下载。
	 * @param returnValue 方法返回值
	 * @param parameter 方法参数
	 * @param mavContainer ModelAndView容器
	 * @param nativeWebRequest 原生Web请求
	 * @throws Exception 处理过程中可能抛出的异常
	 */
	@Override
	public void handleReturnValue(Object returnValue, MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest nativeWebRequest) throws Exception {
		/* check */
		HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		Assert.state(response != null, "No HttpServletResponse");
		ResponseDownload responseDownload = parameter.getMethodAnnotation(ResponseDownload.class);
		Assert.state(responseDownload != null, "No @ResponseDownload");
		mavContainer.setRequestHandled(true);
		downloadHandlerChain.process(returnValue, response, responseDownload);
	}

}
