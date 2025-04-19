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
	 * 不带返回值的方法函数
	 * @param sftp sftp执行器
	 */
	void handle(ISftpExecutor sftp);

}
