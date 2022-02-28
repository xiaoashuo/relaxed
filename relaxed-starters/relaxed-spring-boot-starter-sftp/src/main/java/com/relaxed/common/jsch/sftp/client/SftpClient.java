package com.relaxed.common.jsch.sftp.client;

import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.functions.Handler;
import com.relaxed.common.jsch.sftp.delegates.DelegateHandler;
import com.relaxed.common.jsch.sftp.delegates.DelegateSupplyHandler;
import com.relaxed.common.jsch.sftp.factory.SftpPool;
import com.relaxed.common.jsch.sftp.functions.SupplyHandler;
import com.relaxed.common.jsch.sftp.factory.AbstractSftp;
import lombok.Data;
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
	public void open(Handler handler) {
		AbstractSftp sftp = null;
		try {
			sftp = sftpPool.borrowObject();
			DelegateHandler policyHandler = new DelegateHandler(handler);
			policyHandler.handle(sftp);
		}
		catch (Exception e) {
			throw new SftpClientException("proxy sftp exception", e);
		}
		finally {
			if (sftp != null) {
				sftpPool.returnObject(sftp);
			}
		}

	}

	@Override
	public <T> T supplyOpen(SupplyHandler supplyHandler) {
		AbstractSftp sftp = null;
		try {
			sftp = sftpPool.borrowObject();
			DelegateSupplyHandler policyHandler = new DelegateSupplyHandler(supplyHandler);
			return (T) policyHandler.supplyHandle(sftp);

		}
		catch (Exception e) {
			throw new SftpClientException("proxy sftp exception", e);
		}
		finally {
			if (sftp != null) {
				sftpPool.returnObject(sftp);
			}
		}
	}

}
