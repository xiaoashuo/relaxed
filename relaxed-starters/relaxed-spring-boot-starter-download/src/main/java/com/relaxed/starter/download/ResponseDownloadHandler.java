package com.relaxed.starter.download;

import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import com.relaxed.starter.download.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 响应下载处理器配置类 提供多种文件下载处理器的自动配置，包括本地文件、SFTP和OSS下载
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Configuration
public class ResponseDownloadHandler {

	/**
	 * 默认下载处理器注册
	 * @return 默认下载处理器实现
	 */
	@Bean
	@ConditionalOnMissingBean
	public DownloadHandler defaultDownloadHandler() {
		return new DefaultDownloadHandler();
	}

	/**
	 * 创建下载处理器链
	 * @param downloadHandler 下载处理器
	 * @return DownloadHandlerChain 下载处理器链实例
	 */
	@Bean
	public DownloadHandlerChain downloadHandlerChain(DownloadHandler downloadHandler) {
		return new DownloadHandlerChain(downloadHandler);
	}

	/**
	 * 创建响应下载返回值处理器
	 * @param downloadHandlerChain 下载处理器链
	 * @return ResponseDownloadReturnValueHandler 响应下载返回值处理器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseDownloadReturnValueHandler responseDownloadReturnValueHandler(
			DownloadHandlerChain downloadHandlerChain) {
		return new ResponseDownloadReturnValueHandler(downloadHandlerChain);
	}

}
