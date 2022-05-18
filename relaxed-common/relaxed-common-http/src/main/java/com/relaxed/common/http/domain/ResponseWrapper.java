package com.relaxed.common.http.domain;

import lombok.Data;

/**
 * @author Yakir
 * @Topic ResponseWrapper
 * @Description
 * @date 2022/5/18 13:07
 * @Version 1.0
 */
@Data
public class ResponseWrapper {

	/**
	 * 响应体
	 */
	private String body;

	/**
	 * 文件字节
	 */
	private byte[] fileStream;

}
