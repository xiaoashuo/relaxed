package com.relaxed.common.tenant.exception;

/**
 * TenantException
 *
 * @author Yakir
 */
public class TenantException extends RuntimeException {

	public TenantException(String message) {
		super(message);
	}

	public TenantException(String message, Throwable cause) {
		super(message, cause);
	}

}
