package com.relaxed.common.jsch.sftp.client;

import com.relaxed.common.jsch.sftp.delegates.DelegateHandler;
import com.relaxed.common.jsch.sftp.exception.SftpClientException;
import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;
import com.relaxed.common.jsch.sftp.delegates.DelegateSupplyHandler;
import com.relaxed.common.jsch.sftp.factory.SftpPool;
import com.relaxed.common.jsch.sftp.functions.Handler;
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

	/**
	 * 打开SFTP连接并执行操作
	 * @param handler 操作处理器
	 * @throws SftpClientException 执行操作时可能抛出的异常
	 */
	@Override
	public void open(Handler handler) {
		ISftpExecutor sftp = null;
		try {
			sftp = sftpPool.borrowObject();
			DelegateHandler policyHandler = new DelegateHandler(handler);
			policyHandler.handle(sftp);
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

	/**
	 * 打开SFTP连接并执行操作，返回结果
	 * @param supplyHandler 操作处理器
	 * @param <U> 返回类型
	 * @return 操作结果
	 * @throws SftpClientException 执行操作时可能抛出的异常
	 */
	@Override
	public <U> U supplyOpen(SupplyHandler<U> supplyHandler) {
		ISftpExecutor sftp = null;
		try {
			sftp = sftpPool.borrowObject();
			DelegateSupplyHandler<U> policyHandler = new DelegateSupplyHandler<>(supplyHandler);
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
