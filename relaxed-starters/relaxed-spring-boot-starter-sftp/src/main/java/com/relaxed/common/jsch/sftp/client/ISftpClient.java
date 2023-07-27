package com.relaxed.common.jsch.sftp.client;

import com.relaxed.common.jsch.sftp.functions.Handler;
import com.relaxed.common.jsch.sftp.functions.SupplyHandler;

/**
 * sftp client interface
 *
 * @author shuoyu
 */
public interface ISftpClient {

	/**
	 * 打开sftp 执行操作 带返回值
	 * @param supplyHandler
	 * @return
	 */
	<U> U exec(SupplyHandler<U> supplyHandler);

}
