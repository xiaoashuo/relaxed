package com.relaxed.common.exception.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic ExceptionNoticeResult
 * @Description
 * @date 2022/1/20 10:41
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class ExceptionNoticeResult {

	/**
	 * 通知类型
	 */
	private String channel;

	/**
	 * 是否通知成功
	 */
	private boolean success;

	/**
	 * 异常消息
	 */
	private String errMsg;

	public static ExceptionNoticeResult ofFail(String type, String message) {
		return new ExceptionNoticeResult().setChannel(type).setSuccess(false).setErrMsg(message);
	}

}
