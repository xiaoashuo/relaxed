package com.relaxed.common.log.operation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic LogStatusEnum
 * @Description
 * @date 2021/6/27 12:49
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum LogStatusEnum {

	/**
	 * 成功
	 */
	SUCCESS(1),
	/**
	 * 失败
	 */
	FAIL(0);

	private final int value;

}
