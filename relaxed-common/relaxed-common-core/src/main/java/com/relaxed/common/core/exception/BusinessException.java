package com.relaxed.common.core.exception;

import cn.hutool.core.util.StrUtil;
import com.relaxed.common.model.result.ResultCode;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * 通用业务异常类，用于处理业务逻辑中的异常情况
 *
 * @author Yakir
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

	private final String message;

	private final int code;

	public BusinessException(ResultCode resultCode) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public BusinessException(ResultCode resultCode, Throwable e) {
		super(resultCode.getMessage(), e);
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

	public BusinessException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public BusinessException(int code, String message, Throwable e) {
		super(message, e);
		this.code = code;
		this.message = message;
	}

	public BusinessException(ResultCode resultCode, Throwable e, Object... args) {
		this(resultCode.getCode(), StrUtil.format(resultCode.getMessage(), args), e);
	}

	public BusinessException(int code, String messageTemplate, Object... args) {
		this(code, StrUtil.format(messageTemplate, args), MessageFormatter.getThrowableCandidate(args));
	}

}
