package com.relaxed.common.jsch.sftp.functions;

import com.relaxed.common.jsch.sftp.factory.AbstractSftp;

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
	void handle(AbstractSftp sftp);

}
