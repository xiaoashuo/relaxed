package com.relaxed.common.jsch.sftp.functions;

import com.relaxed.common.jsch.sftp.factory.AbstractSftp;

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
	T supplyHandle(AbstractSftp sftp);

}
