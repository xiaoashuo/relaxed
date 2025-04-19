package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;
import com.relaxed.common.jsch.sftp.SftpProperties;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

/**
 * isfp 提供者
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface ISftpProvider {

	/**
	 * 根据channel sftp 生成Sftp
	 * @param channelSftp sftp通道
	 * @param sftpProperties sftp配置属性
	 * @return sftp执行类
	 */
	ISftpExecutor provide(ChannelSftp channelSftp, SftpProperties sftpProperties);

}
