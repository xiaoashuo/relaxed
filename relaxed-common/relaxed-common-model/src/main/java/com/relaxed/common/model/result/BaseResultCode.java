package com.relaxed.common.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 项目使用基础返回码
 * @author Yakir
 */
@RequiredArgsConstructor
@Getter
public enum BaseResultCode implements ResultCode {

	/**
	 * 更新数据库错误
	 */
	UPDATE_DATABASE_ERROR(90001, "Update Database Error"),

	/**
	 * 恶意请求
	 */
	MALICIOUS_REQUEST(90005, "Malicious Request"),
	/**
	 * 重复执行
	 */
	REPEATED_EXECUTE(90007, "Repeated execute"),;

	private final Integer code;

	private final String message;

}
