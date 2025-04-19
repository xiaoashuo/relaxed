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
	 * 打开sftp 执行操作
	 * @param handler sftp执行操作
	 */
	void open(Handler handler);

	/**
	 * 打开sftp 执行操作 带返回值
	 * @param <U> 返回值的类型参数
	 * @param supplyHandler sftp执行操作 支持返回参数
	 * @return 处理后的结果
	 */
	<U> U supplyOpen(SupplyHandler<U> supplyHandler);

}
