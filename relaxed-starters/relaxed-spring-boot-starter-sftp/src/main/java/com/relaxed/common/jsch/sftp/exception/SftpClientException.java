package com.relaxed.common.jsch.sftp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * sftp exception
 *
 * @author shuoyu
 */
public class SftpClientException extends RuntimeException {

	public SftpClientException(String message) {
		super(message);
	}

	public SftpClientException(Throwable cause) {
		super(cause);
	}

	public SftpClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
