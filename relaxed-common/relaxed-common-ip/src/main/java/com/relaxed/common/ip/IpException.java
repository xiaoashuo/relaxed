package com.relaxed.common.ip;

/**
 * IpException
 *
 * @author Yakir
 */
public class IpException extends RuntimeException {

	public IpException(String message) {
		super(message);
	}

	public IpException(String message, Throwable cause) {
		super(message, cause);
	}

}
