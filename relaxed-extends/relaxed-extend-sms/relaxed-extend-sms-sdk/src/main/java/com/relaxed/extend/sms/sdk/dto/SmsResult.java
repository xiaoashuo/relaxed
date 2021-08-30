package com.relaxed.extend.sms.sdk.dto;

import com.relaxed.common.model.result.R;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author Yakir
 * @Topic SmsResult
 * @Description
 * @date 2021/8/26 14:54
 * @Version 1.0
 */
@Getter
@Setter
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel" })
@Accessors(chain = true)
public class SmsResult<T> {

	public static final String DEF_ERROR_MESSAGE = "系统繁忙，请稍候再试";

	public static final String HYSTRIX_ERROR_MESSAGE = "请求超时，请稍候再试";

	public static final int SUCCESS_CODE = 0;

	public static final int FAIL_CODE = -1;

	public static final int TIMEOUT_CODE = -2;

	/**
	 * 统一参数验证异常
	 */
	public static final int VALID_EX_CODE = -9;

	public static final int OPERATION_EX_CODE = -10;

	/**
	 * 调用是否成功标识，0：成功，-1:系统繁忙，此时请开发者稍候再试 详情见[ExceptionCode]
	 */
	private Integer code;

	/**
	 * 结果消息，如果调用成功，消息通常为空T
	 */
	private String msg;

	/**
	 * 调用路径
	 */
	private String path;

	/**
	 * 调用结果
	 */
	private T data;

	/**
	 * 附加数据
	 */
	private Map<String, Object> extra;

	public SmsResult(int code, T data, String msg) {
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	public static <E> SmsResult<E> fail(String msg) {
		return fail(OPERATION_EX_CODE, msg);
	}

	/**
	 * 请求失败消息
	 * @param msg
	 * @return
	 */
	public static <E> SmsResult<E> fail(int code, String msg) {
		return new SmsResult<>(code, null, (msg == null || msg.isEmpty()) ? DEF_ERROR_MESSAGE : msg);
	}

}
