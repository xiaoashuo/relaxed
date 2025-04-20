package com.relaxed.common.core.exception;

import com.relaxed.common.model.result.SysResultCode;

/**
 * SQL注入检查异常，用于处理SQL注入相关的异常情况
 *
 * @author Hccake
 * @since 1.0.0
 */
public class SqlCheckedException extends BusinessException {

	public SqlCheckedException(SysResultCode systemResultMsg) {
		super(systemResultMsg);
	}

	public SqlCheckedException(SysResultCode systemResultMsg, Throwable e) {
		super(systemResultMsg, e);
	}

	public SqlCheckedException(int code, String msg) {
		super(code, msg);
	}

	public SqlCheckedException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

}
