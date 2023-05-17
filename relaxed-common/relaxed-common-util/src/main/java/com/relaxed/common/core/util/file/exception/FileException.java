package com.relaxed.common.core.util.file.exception;


import cn.hutool.core.util.StrUtil;

/**
 * @author Yakir
 * @Topic FileException
 * @Description
 * @date 2022/11/27 12:19
 * @Version 1.0
 */
public class FileException extends RuntimeException {

	private final String message;
	private final int code;


	public FileException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}
	public FileException(int code, String messageTemplate, Object... args) {
		this(code, StrUtil.format(messageTemplate, args));
	}

	@Override
	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}
}
