package com.relaxed.common.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 项目基础返回码
 * <p>
 * 定义了项目中常用的基础业务返回码，用于表示常见的业务操作结果状态。 这些返回码通常用于表示业务层面的错误或异常情况。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
public enum BaseResultCode implements ResultCode {

	/**
	 * 数据库操作异常
	 * <p>
	 * 表示在执行数据库保存或更新操作时发生异常。
	 * </p>
	 */
	UPDATE_DATABASE_ERROR(90001, "Update Database Error"),

	/**
	 * 业务逻辑校验异常
	 * <p>
	 * 表示在执行业务逻辑校验时发现不符合业务规则的情况。
	 * </p>
	 */
	LOGIC_CHECK_ERROR(90004, "Logic Check Error"),

	/**
	 * 恶意请求异常
	 * <p>
	 * 表示检测到可能是恶意的请求行为。
	 * </p>
	 */
	MALICIOUS_REQUEST(90005, "Malicious Request"),

	/**
	 * 文件上传异常
	 * <p>
	 * 表示在文件上传过程中发生异常。
	 * </p>
	 */
	FILE_UPLOAD_ERROR(90006, "File Upload Error"),

	/**
	 * 重复执行异常
	 * <p>
	 * 表示检测到重复执行的操作请求。
	 * </p>
	 */
	REPEATED_EXECUTE(90007, "Repeated execute"),

	/**
	 * 未知异常
	 * <p>
	 * 表示发生了未预期的异常情况。
	 * </p>
	 */
	UNKNOWN_ERROR(99999, "Unknown Error");

	/**
	 * 错误码
	 */
	private final Integer code;

	/**
	 * 错误描述
	 */
	private final String message;

}
