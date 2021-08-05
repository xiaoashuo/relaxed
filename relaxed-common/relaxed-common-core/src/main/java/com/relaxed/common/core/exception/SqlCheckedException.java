package com.relaxed.common.core.exception;

import com.relaxed.common.model.result.SysResultCode;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/19 16:52 sql防注入校验异常
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
