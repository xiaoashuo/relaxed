package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;
import com.relaxed.common.jsch.sftp.SftpProperties;

/**
 * SFTP执行器抽象类
 *
 * @author shuoyu
 */

public abstract class AbstractSftpExecutor implements ISftpExecutor {

	protected final ChannelSftp channelSftp;

	private final SftpProperties sftpProperties;

	/**
	 * 创建SFTP执行器抽象类实例
	 * @param channelSftp SFTP通道
	 * @param sftpProperties SFTP配置属性
	 */
	protected AbstractSftpExecutor(ChannelSftp channelSftp, SftpProperties sftpProperties) {
		this.channelSftp = channelSftp;
		this.sftpProperties = sftpProperties;
	}

	/**
	 * 获取SFTP通道
	 * @return SFTP通道
	 */
	@Override
	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	/**
	 * 获取SFTP配置属性
	 * @return SFTP配置属性
	 */
	protected SftpProperties getSftpProperties() {
		return sftpProperties;
	}

}
