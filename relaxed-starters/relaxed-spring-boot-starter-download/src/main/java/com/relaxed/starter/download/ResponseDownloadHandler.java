package com.relaxed.starter.download;

import cn.hutool.core.util.ClassUtil;
import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.common.core.util.file.FileUtils;
import com.relaxed.common.jsch.sftp.SftpAutoConfiguration;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.jsch.sftp.client.SftpClient;
import com.relaxed.common.oss.s3.OssClient;
import com.relaxed.starter.download.aop.ResponseDownloadReturnValueHandler;
import com.relaxed.starter.download.enums.DownTypeEnum;
import com.relaxed.starter.download.handler.*;
import com.relaxed.starter.download.handler.ext.OssFileHandler;
import com.relaxed.starter.download.handler.ext.SftpFileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
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
