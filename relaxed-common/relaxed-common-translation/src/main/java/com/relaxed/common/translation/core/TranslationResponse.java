package com.relaxed.common.translation.core;

import lombok.Data;

/**
 * 翻译响应结果封装类。 用于封装翻译服务的响应结果，包含状态码、消息和实际数据。 提供了成功响应的快速构建方法和状态检查方法。
 *
 * @param <T> 响应数据的类型
 * @author Yakir
 * @since 1.0
 */
@Data
public class TranslationResponse<T> {

	/**
	 * 成功状态码
	 */
	public static final Integer SUCCESS_CODE = 200;

	/**
	 * 响应状态码
	 */
	private Integer code;

	/**
	 * 响应消息
	 */
	private String msg;

	/**
	 * 响应数据
	 */
	private T data;

	/**
	 * 构建成功响应
	 * @param data 响应数据
	 * @param <T> 响应数据类型
	 * @return 成功响应对象
	 */
	public static <T> TranslationResponse ok(T data) {
		TranslationResponse<T> response = new TranslationResponse<>();
		response.setCode(SUCCESS_CODE);
		response.setMsg("success");
		response.setData(data);
		return response;
	}

	/**
	 * 检查响应是否成功
	 * @return 如果状态码等于成功状态码则返回true，否则返回false
	 */
	public boolean success() {
		return SUCCESS_CODE.equals(this.code);
	}

}
