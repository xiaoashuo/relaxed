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
public class DelegateSupplyHandler<T> implements SupplyHandler {

	private final SupplyHandler target;

	@Override
	public T supplyHandle(ISftpExecutor sftp) {
		return (T) target.supplyHandle(sftp);
	}

}
