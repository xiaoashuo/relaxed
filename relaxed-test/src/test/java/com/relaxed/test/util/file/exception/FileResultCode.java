package com.relaxed.test.util.file.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic FileResultCode
 * @Description
 * @date 2023/5/17 11:10
 * @Version 1.0
 */
@RequiredArgsConstructor
@Getter
public enum FileResultCode {

	FILE_PARAM_ERROR(400010, "文件参数异常"),;

	private final Integer code;

	private final String message;

}
