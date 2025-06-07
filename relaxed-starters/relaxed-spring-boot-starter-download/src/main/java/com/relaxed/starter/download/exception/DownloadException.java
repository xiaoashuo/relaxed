package com.relaxed.starter.download.exception;

/**
 * 文件下载异常类，用于表示文件下载过程中发生的错误。 继承自 {@link RuntimeException}，表示这是一个非受检异常。
 *
 * @author Yakir
 * @since 1.0
 */
public class DownloadException extends RuntimeException {

	/**
	 * 使用指定的错误消息构造下载异常
	 * @param message 错误消息，描述异常的原因
	 */
	public DownloadException(String message) {
		super(message);
	}

	public DownloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
