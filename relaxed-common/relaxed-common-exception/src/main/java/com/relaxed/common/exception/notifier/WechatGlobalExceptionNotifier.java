package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import com.relaxed.extend.dingtalk.message.DingTalkTextMessage;
import com.relaxed.extend.dingtalk.request.DingTalkResponse;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import com.relaxed.extend.wechat.message.WechatTextMessage;
import com.relaxed.extend.wechat.request.WechatResponse;
import com.relaxed.extend.wechat.request.WechatSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * wechat消息通知
 *
 * @author Yakir
 */
@Slf4j
@Getter
public class WechatGlobalExceptionNotifier extends AbstractExceptionNotifier {

	private final WechatSender sender;

	public WechatGlobalExceptionNotifier(String channel, String applicationName, WechatSender sender) {
		super(channel, applicationName);
		this.sender = sender;

	}

	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		WechatResponse response = sender
				.sendMessage(new WechatTextMessage().setContent(sendMessage.toString()).atAll());
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(response.isSuccess())
				.setErrMsg(response.getResponse());
	}

}
