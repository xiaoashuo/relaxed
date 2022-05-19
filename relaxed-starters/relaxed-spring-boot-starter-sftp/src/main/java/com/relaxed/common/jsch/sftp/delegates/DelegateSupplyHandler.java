package com.relaxed.common.jsch.sftp.delegates;

import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.functions.SupplyHandler;
import com.relaxed.common.jsch.sftp.factory.AbstractSftp;
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
	public T supplyHandle(AbstractSftp sftp) {
		return (T) target.supplyHandle(sftp);
	}

}
