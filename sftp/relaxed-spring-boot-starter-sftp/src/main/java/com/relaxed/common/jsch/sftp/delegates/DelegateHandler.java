package com.relaxed.common.jsch.sftp.delegates;

import com.relaxed.common.jsch.sftp.executor.ISftpExecutor;

import com.relaxed.common.jsch.sftp.functions.Handler;
import lombok.RequiredArgsConstructor;

/**
 * 不带返回值函数的委托类
 *
 * @author shuoyu
 */
@RequiredArgsConstructor
public class DelegateHandler implements Handler {

	private final Handler target;

	@Override
	public void handle(ISftpExecutor sftp) {
		target.handle(sftp);
	}

}
