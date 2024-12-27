package com.relaxed.common.jsch.sftp.delegates;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

import com.relaxed.common.jsch.sftp.functions.SupplyHandler;
import lombok.RequiredArgsConstructor;

/**
 * 带返回值的委托类
 *
 * @author shuoyu
 */
@RequiredArgsConstructor
public class DelegateSupplyHandler<U> implements SupplyHandler<U> {

	private final SupplyHandler<U> target;

	@Override
	public U supplyHandle(ISftpExecutor sftp) {
		return target.supplyHandle(sftp);
	}

}
