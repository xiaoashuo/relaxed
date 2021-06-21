package com.relaxed.common.jsch.sftp.factory;

import com.jcraft.jsch.ChannelSftp;

/**
 * isfp 提供者
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface ISftpProvider {

	/**
	 * 根据channel sftp 生成Sftp
	 * @param channelSftp
	 * @return
	 */
	AbstractSftp provide(ChannelSftp channelSftp);

}
