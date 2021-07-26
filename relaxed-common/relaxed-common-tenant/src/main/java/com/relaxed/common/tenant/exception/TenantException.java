package com.relaxed.common.tenant.exception;

/**
 * @author Yakir
 * @Topic TenantException
 * @Description
 * @date 2021/7/26 17:09
 * @Version 1.0
 */
public class TenantException extends RuntimeException {

	public TenantException(String message) {
		super(message);
	}

	public TenantException(String message, Throwable cause) {
		super(message, cause);
	}

}
