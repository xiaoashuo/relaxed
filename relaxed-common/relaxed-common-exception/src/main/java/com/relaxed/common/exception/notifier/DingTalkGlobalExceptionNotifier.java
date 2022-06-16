package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import com.relaxed.extend.dingtalk.message.DingTalkTextMessage;
import com.relaxed.extend.dingtalk.request.DingTalkResponse;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
@Getter
public class DingTalkGlobalExceptionNotifier extends AbstractExceptionNotifier {

	private final DingTalkSender sender;

	public DingTalkGlobalExceptionNotifier(String channel, String applicationName, DingTalkSender sender) {
		super(channel, applicationName);
		this.sender = sender;

	}

	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		DingTalkResponse response = sender
				.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()).atAll());
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(response.isSuccess())
				.setErrMsg(response.getResponse());
	}

}
