package com.relaxed.common.jsch.sftp;

import com.relaxed.common.jsch.sftp.client.ISftpClient;
import com.relaxed.common.jsch.sftp.client.SftpClient;
import com.relaxed.common.jsch.sftp.executor.ISftpProvider;
import com.relaxed.common.jsch.sftp.executor.SftpExecutor;
import com.relaxed.common.jsch.sftp.factory.*;

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
	 * sftp provider 主要负责提供产出{@link AbstractSql}子实列的动作
	 * @return sftpExceutor创建者
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISftpProvider iSftpProvider() {
		return SftpExecutor::new;
	}

	/**
	 * sftp 客户端
	 * @param sftpProperties sftp链接属性
	 * @param iSftpProvider sftp工具类提供者
	 * @return sftp客户端
	 */
	@Bean
	@ConditionalOnMissingBean
	public ISftpClient sftpClient(SftpProperties sftpProperties, ISftpProvider iSftpProvider) {
		SftpPool sftpPool = new SftpPool(SftpFactory.of(sftpProperties, iSftpProvider),
				SftpPoolConfig.of(sftpProperties), SftpAbandonedConfig.of(sftpProperties));
		return new SftpClient(sftpPool);
	}

}
