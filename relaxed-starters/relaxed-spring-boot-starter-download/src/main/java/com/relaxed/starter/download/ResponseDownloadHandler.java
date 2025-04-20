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
 * 响应下载处理器配置类 提供多种文件下载处理器的自动配置，包括本地文件、SFTP和OSS下载
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
@Configuration
public class ResponseDownloadHandler {

	/**
	 * 创建本地文件下载处理器
	 * @return LocalDownloadHandler 本地文件下载处理器实例
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
		 * 创建SFTP文件下载处理器
		 * @param iSftpClient SFTP客户端
		 * @return SftpDownloadHandler SFTP文件下载处理器实例
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
		 * 创建OSS文件下载处理器
		 * @param ossClient OSS客户端
		 * @return OssDownloadHandler OSS文件下载处理器实例
		 */
		@Bean
		@ConditionalOnMissingBean(OssDownloadHandler.class)
		public OssDownloadHandler ossDownloadHandler(OssClient ossClient) {
			return new OssDownloadHandler(ossClient);
		}

	}

	/**
	 * 创建下载处理器链
	 * @param downloadHandlerList 所有可用的下载处理器
	 * @return DownloadHandlerChain 下载处理器链实例
	 */
	@Bean
	public DownloadHandlerChain downloadHandlerChain(List<DownloadHandler> downloadHandlerList) {
		return new DownloadHandlerChain(downloadHandlerList);
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
