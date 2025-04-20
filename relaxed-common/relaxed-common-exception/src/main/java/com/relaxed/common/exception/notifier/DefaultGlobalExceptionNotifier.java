package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认全局异常通知器
 * <p>
 * 实现了 {@link ExceptionNotifier} 接口，提供了默认的异常通知实现。 主要用于记录异常日志，不进行实际的通知发送。
 * </p>
 *
 * @author lingting
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Getter
@Slf4j
public class DefaultGlobalExceptionNotifier implements ExceptionNotifier {

	/**
	 * 通知渠道
	 * <p>
	 * 默认为"DEFAULT"。
	 * </p>
	 */
	private final String channel;

	/**
	 * 默认构造函数
	 * <p>
	 * 初始化通知渠道为"DEFAULT"。
	 * </p>
	 */
	public DefaultGlobalExceptionNotifier() {
		this.channel = "DEFAULT";
	}

	/**
	 * 发送异常通知
	 * <p>
	 * 记录异常日志，并返回成功的结果。
	 * </p>
	 * @param sendMessage 需要发送的异常消息
	 * @return 通知发送结果
	 */
	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		log.error("默认异常通知,消息{}", sendMessage);
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(true);
	}

}
