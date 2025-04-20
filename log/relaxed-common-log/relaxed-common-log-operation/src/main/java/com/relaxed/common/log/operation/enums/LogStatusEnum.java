package com.relaxed.common.log.operation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 操作日志状态枚举 用于标识操作日志的执行状态,包括成功和失败两种状态
 *
 * @author Yakir
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
public enum LogStatusEnum {

	/**
	 * 操作执行成功
	 */
	SUCCESS(1),

	/**
	 * 操作执行失败
	 */
	FAIL(0);

	/**
	 * 状态值
	 */
	private final int value;

}
