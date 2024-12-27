package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;
import com.relaxed.common.jsch.sftp.SftpProperties;

/**
 * sftp 抽象类
 *
 * @author shuoyu
 */

public abstract class AbstractSftpExecutor implements ISftpExecutor {

	protected final ChannelSftp channelSftp;

	private final SftpProperties sftpProperties;

	protected AbstractSftpExecutor(ChannelSftp channelSftp, SftpProperties sftpProperties) {
		this.channelSftp = channelSftp;
		this.sftpProperties = sftpProperties;
	}

	/**
	 * 获取channel sftp 仅允许 同包路径或子类调用
	 * @return
	 */
	@Override
	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	protected SftpProperties getSftpProperties() {
		return sftpProperties;
	}

}
