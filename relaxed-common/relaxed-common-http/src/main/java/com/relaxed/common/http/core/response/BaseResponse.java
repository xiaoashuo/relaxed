package com.relaxed.common.http.core.response;

import lombok.Data;

/**
 * @author Yakir
 * @Topic BaseResponse
 * @Description
 * @date 2022/4/24 15:43
 * @Version 1.0
 */
@Data
public class BaseResponse implements IResponse {

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 消息
	 */
	private String message;

}
