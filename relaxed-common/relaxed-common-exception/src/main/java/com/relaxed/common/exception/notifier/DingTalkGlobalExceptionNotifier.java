package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import com.relaxed.extend.dingtalk.message.DingTalkTextMessage;
import com.relaxed.extend.dingtalk.request.DingTalkResponse;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉异常通知器
 * <p>
 * 继承自 {@link AbstractExceptionNotifier}，实现了通过钉钉发送异常通知的功能。 使用 {@link DingTalkSender}
 * 发送异常信息到钉钉群。
 * </p>
 *
 * @author lingting
 * @since 1.0.0
 */
@Slf4j
@Getter
public class DingTalkGlobalExceptionNotifier extends AbstractExceptionNotifier {

	/**
	 * 钉钉发送器
	 * <p>
	 * 用于发送异常通知到钉钉群。
	 * </p>
	 */
	private final DingTalkSender sender;

	/**
	 * 构造函数
	 * <p>
	 * 初始化钉钉通知器的相关配置。
	 * </p>
	 * @param channel 通知渠道
	 * @param applicationName 应用名称
	 * @param sender 钉钉发送器
	 */
	public DingTalkGlobalExceptionNotifier(String channel, String applicationName, DingTalkSender sender) {
		super(channel, applicationName);
		this.sender = sender;
	}

	/**
	 * 发送异常通知
	 * <p>
	 * 通过钉钉发送异常信息，并返回发送结果。 使用文本消息格式，并@所有人。
	 * </p>
	 * @param sendMessage 需要发送的异常消息
	 * @return 通知发送结果
	 */
	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		DingTalkResponse response = sender
				.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()).atAll());
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(response.isSuccess())
				.setErrMsg(response.getResponse());
	}

}
