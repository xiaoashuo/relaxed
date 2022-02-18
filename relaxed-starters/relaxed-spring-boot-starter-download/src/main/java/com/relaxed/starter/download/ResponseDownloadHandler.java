package com.relaxed.starter.download;

import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import com.relaxed.starter.download.handler.DownloadHandler;
import com.relaxed.starter.download.handler.LocalDownloadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Yakir
 * @Topic ResponseDownHandler
 * @Description
 * @date 2022/2/18 17:53
 * @Version 1.0
 */
@RequiredArgsConstructor
@Configuration
public class ResponseDownloadHandler {

	/**
	 * 本地下载处理器
	 * @author yakir
	 * @date 2022/2/18 17:54
	 * @return com.relaxed.starter.download.handler.LocalDownloadHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public LocalDownloadHandler localDownloadHandler() {
		return new LocalDownloadHandler();
	}

	/**
	 * 响应下载处理器
	 * @author yakir
	 * @date 2022/2/18 18:04
	 * @param downloadHandlerList
	 * @return com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseDownloadReturnValueHandler responseDownloadReturnValueHandler(
			List<DownloadHandler> downloadHandlerList) {
		return new ResponseDownloadReturnValueHandler(downloadHandlerList);
	}

}
