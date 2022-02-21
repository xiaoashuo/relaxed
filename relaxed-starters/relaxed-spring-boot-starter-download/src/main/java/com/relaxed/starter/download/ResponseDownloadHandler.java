package com.relaxed.starter.download;

import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.oss.s3.OssClient;
import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import com.relaxed.starter.download.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

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
	@ConditionalOnMissingBean(LocalDownloadHandler.class)
	public LocalDownloadHandler localDownloadHandler() {
		return new LocalDownloadHandler();
	}

	@Configuration
	@ConditionalOnClass(ISftpClient.class)
	public static class ISftpRegister {

		/**
		 * sftp下载器
		 * @param iSftpClient
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(SftpDownloadHandler.class)
		public SftpDownloadHandler sftpDownloadHandler(ISftpClient iSftpClient) {
			return new SftpDownloadHandler(iSftpClient);
		}

	}

	@Configuration
	@ConditionalOnClass(OssClient.class)
	public static class OssRegister {

		/**
		 * oss 下载器
		 * @param ossClient
		 * @return
		 */
		@Bean
		@ConditionalOnMissingBean(OssDownloadHandler.class)
		public OssDownloadHandler ossDownloadHandler(OssClient ossClient) {
			return new OssDownloadHandler(ossClient);
		}

	}

	/**
	 * 下载处理器链
	 * @param downloadHandlerList
	 * @return
	 */
	@Bean
	public DownloadHandlerChain downloadHandlerChain(List<DownloadHandler> downloadHandlerList) {
		return new DownloadHandlerChain(downloadHandlerList);
	}

	/**
	 * 响应下载处理器
	 * @author yakir
	 * @date 2022/2/18 18:04
	 * @param downloadHandlerChain
	 * @return com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseDownloadReturnValueHandler responseDownloadReturnValueHandler(
			DownloadHandlerChain downloadHandlerChain) {
		return new ResponseDownloadReturnValueHandler(downloadHandlerChain);
	}

}
