package com.relaxed.common.jsch.sftp.functions;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

/**
 * 处理带返回值动作
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface SupplyHandler<T> {

	/**
	 * 带返回值的
	 * @param sftp
	 * @param <T>
	 * @return
	 */
	T supplyHandle(ISftpExecutor sftp);

}
