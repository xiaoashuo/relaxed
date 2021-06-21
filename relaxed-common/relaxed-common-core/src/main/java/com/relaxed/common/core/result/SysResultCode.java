package com.relaxed.common.core.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统通用返回结果码
 * @author Yakir
 */
@RequiredArgsConstructor
@Getter
public enum SysResultCode implements ResultCode {

	// ================ 基础部分，参考 HttpStatus =============
	/**
	 * 成功
	 */
	SUCCESS(200, "Success"),
	/**
	 * 参数错误
	 */
	BAD_REQUEST(400, "Bad Request"),
	/**
	 * 未认证
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	/**
	 * 未授权
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * 服务异常
	 */
	SERVER_ERROR(500, "Internal Server Error");

	private final Integer code;

	private final String message;

}
