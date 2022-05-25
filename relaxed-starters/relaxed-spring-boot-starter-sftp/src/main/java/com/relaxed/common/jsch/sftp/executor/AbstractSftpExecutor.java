package com.relaxed.common.jsch.sftp.executor;

import com.jcraft.jsch.ChannelSftp;

/**
 * sftp 抽象类
 *
 * @author shuoyu
 */

public abstract class AbstractSftpExecutor implements ISftpExecutor {

	protected final ChannelSftp channelSftp;

	protected AbstractSftpExecutor(ChannelSftp channelSftp) {
		this.channelSftp = channelSftp;
	}

	/**
	 * 获取channel sftp 仅允许 同包路径或子类调用
	 * @return
	 */
	public final ChannelSftp getChannelSftp() {
		return channelSftp;
	}

}
