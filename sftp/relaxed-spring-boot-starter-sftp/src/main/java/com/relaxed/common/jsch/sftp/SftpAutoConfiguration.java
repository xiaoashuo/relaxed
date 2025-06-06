package com.relaxed.common.jsch.sftp;

import com.relaxed.common.core.util.file.FileHandlerLoader;
import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.jsch.sftp.client.SftpClient;
import com.relaxed.common.jsch.sftp.executor.ISftpProvider;
import com.relaxed.common.jsch.sftp.executor.SftpExecutor;
import com.relaxed.common.jsch.sftp.factory.*;

import com.relaxed.common.jsch.sftp.handler.SftpFileHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * sftp 自动配置
 *
 * @author shuoyu
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(SftpProperties.class)
public class SftpAutoConfiguration {

	/**
	 * sftp provider
	 * 主要负责提供产出{@link com.relaxed.common.jsch.sftp.executor.ISftpExecutor}子实例的动作
	 * @return sftp执行器创建者
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISftpProvider iSftpProvider() {
		return SftpExecutor::new;
	}

	/**
	 * sftp 客户端
	 * @param sftpProperties sftp连接属性
	 * @param iSftpProvider sftp工具类提供者
	 * @return sftp客户端
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISftpClient sftpClient(SftpProperties sftpProperties, ISftpProvider iSftpProvider) {
		SftpPool sftpPool = new SftpPool(SftpFactory.of(sftpProperties, iSftpProvider),
				SftpPoolConfig.of(sftpProperties), SftpAbandonedConfig.of(sftpProperties));
		SftpClient sftpClient = new SftpClient(sftpPool);
		// 注册sftp文件处理器
		FileHandlerLoader.register(new SftpFileHandler("sftp", sftpClient));
		return sftpClient;
	}

}
