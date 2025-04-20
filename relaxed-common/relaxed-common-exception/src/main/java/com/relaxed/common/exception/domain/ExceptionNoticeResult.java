package com.relaxed.common.exception.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 异常通知结果
 * <p>
 * 用于记录单个通知处理器的执行结果，包括通知类型、执行状态和错误信息。 支持链式调用设置属性。
 * </p>
 *
 * @author Yakir
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class ExceptionNoticeResult {

	/**
	 * 通知类型
	 * <p>
	 * 标识通知的渠道或方式，如邮件、短信等。
	 * </p>
	 */
	private String channel;

	/**
	 * 是否通知成功
	 * <p>
	 * 表示通知是否成功发送。
	 * </p>
	 */
	private boolean success;

	/**
	 * 异常消息
	 * <p>
	 * 当通知失败时，记录具体的错误信息。
	 * </p>
	 */
	private String errMsg;

	/**
	 * 创建失败的通知结果
	 * <p>
	 * 快速创建一个表示通知失败的结果对象。
	 * </p>
	 * @param type 通知类型
	 * @param message 错误信息
	 * @return 失败的通知结果对象
	 */
	public static ExceptionNoticeResult ofFail(String type, String message) {
		return new ExceptionNoticeResult().setChannel(type).setSuccess(false).setErrMsg(message);
	}

}
