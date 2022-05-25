package com.relaxed.common.jsch.sftp.functions;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

/**
 * 处理sftp 不带返回值的动作
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface Handler {

	/**
	 * 不带返回值
	 * @param sftp
	 */
	void handle(ISftpExecutor sftp);

}
