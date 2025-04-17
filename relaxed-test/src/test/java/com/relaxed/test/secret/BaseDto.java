package com.relaxed.test.secret;

import lombok.Data;

/**
 * @author Yakir
 * @Topic BaseDto
 * @Description
 * @date 2021/11/15 10:32
 * @Version 1.0
 */
@Data
public class BaseDto<T> {

	private String test;

	private T content;

}
