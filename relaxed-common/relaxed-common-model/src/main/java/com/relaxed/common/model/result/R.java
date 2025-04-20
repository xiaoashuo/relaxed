package com.relaxed.common.model.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应信息主体
 * <p>
 * 用于封装API接口的统一返回格式，包含状态码、消息和数据。
 * </p>
 *
 * @param <T> 响应数据的类型
 * @author Yakir
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(title = "统一返回体结构")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(title = "状态码", description = "业务状态码，0表示成功，非0表示失败", defaultValue = "0")
	private int code;

	@Schema(title = "消息", description = "业务处理结果描述", defaultValue = "Success")
	private String message;

	@Schema(title = "数据", description = "响应数据，可能为null", nullable = true, defaultValue = "null")
	private T data;

	/**
	 * 创建成功响应，不包含数据
	 * @param <T> 响应数据类型
	 * @return 成功响应对象
	 */
	public static <T> R<T> ok() {
		return ok(null);
	}

	/**
	 * 创建成功响应，包含数据
	 * @param data 响应数据
	 * @param <T> 响应数据类型
	 * @return 成功响应对象
	 */
	public static <T> R<T> ok(T data) {
		return new R<T>().setCode(SysResultCode.SUCCESS.getCode()).setData(data)
				.setMessage(SysResultCode.SUCCESS.getMessage());
	}

	/**
	 * 创建成功响应，包含数据和自定义消息
	 * @param data 响应数据
	 * @param message 自定义消息
	 * @param <T> 响应数据类型
	 * @return 成功响应对象
	 */
	public static <T> R<T> ok(T data, String message) {
		return new R<T>().setCode(SysResultCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	/**
	 * 创建失败响应
	 * @param code 错误码
	 * @param message 错误消息
	 * @param <T> 响应数据类型
	 * @return 失败响应对象
	 */
	public static <T> R<T> failed(int code, String message) {
		return new R<T>().setCode(code).setMessage(message);
	}

	/**
	 * 创建失败响应，使用预定义的错误码
	 * @param failMsg 预定义的错误码对象
	 * @param <T> 响应数据类型
	 * @return 失败响应对象
	 */
	public static <T> R<T> failed(ResultCode failMsg) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(failMsg.getMessage());
	}

	/**
	 * 创建失败响应，使用预定义的错误码和自定义消息
	 * @param failMsg 预定义的错误码对象
	 * @param message 自定义错误消息
	 * @param <T> 响应数据类型
	 * @return 失败响应对象
	 */
	public static <T> R<T> failed(ResultCode failMsg, String message) {
		return new R<T>().setCode(failMsg.getCode()).setMessage(message);
	}

}
