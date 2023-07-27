package com.relaxed.common.jsch.sftp.client;

import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;
import com.relaxed.common.jsch.sftp.delegates.DelegateSupplyHandler;
import com.relaxed.common.jsch.sftp.factory.SftpPool;
import com.relaxed.common.jsch.sftp.functions.SupplyHandler;
import lombok.RequiredArgsConstructor;

/**
 * sftp client 单客户端
 *
 * @author shuoyu
 */
@RequiredArgsConstructor
public class SftpClient implements ISftpClient {

	private final SftpPool sftpPool;

	@Override
	public void open(SupplyHandler<Void> supplyHandler) {
		this.supplyOpen(supplyHandler);
	}

	@Override
	public <U> U supplyOpen(SupplyHandler<U> supplyHandler) {
		ISftpExecutor sftp = null;
		try {
			sftp = sftpPool.borrowObject();
			DelegateSupplyHandler<U> policyHandler = new DelegateSupplyHandler(supplyHandler);
			return policyHandler.supplyHandle(sftp);

		}
		catch (Exception e) {
			throw new SftpClientException(e.getMessage(), e);
		}
		finally {
			if (sftp != null) {
				sftpPool.returnObject(sftp);
			}
		}
	}

}
