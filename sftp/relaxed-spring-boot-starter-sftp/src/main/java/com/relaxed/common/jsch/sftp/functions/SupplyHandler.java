package com.relaxed.common.jsch.sftp.functions;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

/**
 * 处理带返回值动作
 *
 * @author shuoyu
 */
@FunctionalInterface
public interface SupplyHandler<U> {

	/**
	 * 带返回值的
	 * @param sftp sftp执行器
	 * @return 执行结果
	 */
	U supplyHandle(ISftpExecutor sftp);

}
