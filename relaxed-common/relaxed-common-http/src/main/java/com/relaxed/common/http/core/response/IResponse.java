package com.relaxed.common.http.core.response;

/**
 * @author Yakir
 * @Topic IResponse
 * @Description
 * @date 2022/5/18 8:35
 * @Version 1.0
 */
public interface IResponse {

	/**
	 * 获取响应码
	 * @author yakir
	 * @date 2022/5/27 15:33
	 * @return java.lang.Integer
	 */
	Integer getCode();

	/**
	 * 获取响应消息
	 * @author yakir
	 * @date 2022/5/27 15:34
	 * @return java.lang.String
	 */
	String getMessage();

}
