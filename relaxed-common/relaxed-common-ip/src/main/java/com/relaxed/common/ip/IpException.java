package com.relaxed.common.ip;

/**
 * @author Yakir
 * @Topic IpException
 * @Description
 * @date 2021/9/1 11:18
 * @Version 1.0
 */
public class IpException extends RuntimeException {

	public IpException(String message) {
		super(message);
	}

	public IpException(String message, Throwable cause) {
		super(message, cause);
	}

}
