package com.relaxed.common.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 系统通用返回结果码
 * <p>
 * 定义了系统级别的通用返回结果码，参考 HTTP 状态码规范。 这些结果码用于表示系统层面的操作结果状态。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
public enum SysResultCode implements ResultCode {

	// ================ 基础部分，参考 HttpStatus =============
	/**
	 * 操作成功
	 * <p>
	 * 表示请求已成功处理并返回。
	 * </p>
	 */
	SUCCESS(200, "Success"),
	/**
	 * 请求参数错误
	 * <p>
	 * 表示客户端发送的请求参数有误，服务器无法处理。
	 * </p>
	 */
	BAD_REQUEST(400, "Bad Request"),
	/**
	 * 未认证
	 * <p>
	 * 表示请求需要用户认证，但未提供有效的认证信息。
	 * </p>
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	/**
	 * 未授权
	 * <p>
	 * 表示用户已认证，但没有足够的权限执行请求的操作。
	 * </p>
	 */
	FORBIDDEN(403, "Forbidden"),
	/**
	 * 服务器内部错误
	 * <p>
	 * 表示服务器在处理请求时发生了未预期的错误。
	 * </p>
	 */
	SERVER_ERROR(500, "Internal Server Error");

	/**
	 * 状态码
	 */
	private final Integer code;

	/**
	 * 状态描述
	 */
	private final String message;

}
