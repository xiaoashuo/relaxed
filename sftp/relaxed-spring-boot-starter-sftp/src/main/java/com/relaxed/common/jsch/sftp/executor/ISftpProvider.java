package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;
import com.relaxed.common.jsch.sftp.SftpProperties;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

/**
 * SFTP执行器提供者接口
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface ISftpProvider {

	/**
	 * 根据SFTP通道和配置属性创建SFTP执行器
	 * @param channelSftp SFTP通道
	 * @param sftpProperties SFTP配置属性
	 * @return SFTP执行器实例
	 */
	ISftpExecutor provide(ChannelSftp channelSftp, SftpProperties sftpProperties);

}
