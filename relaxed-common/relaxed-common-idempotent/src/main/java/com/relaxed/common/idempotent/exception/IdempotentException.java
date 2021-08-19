package com.relaxed.common.idempotent.exception;

import com.relaxed.common.core.exception.BusinessException;
import lombok.EqualsAndHashCode;

/**
 * @author hccake
 */
@EqualsAndHashCode(callSuper = true)
public class IdempotentException extends BusinessException {

	public IdempotentException(int code, String message) {
		super(code, message);
	}

}
