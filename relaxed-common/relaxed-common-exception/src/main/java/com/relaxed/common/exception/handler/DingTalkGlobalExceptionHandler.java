package com.relaxed.common.exception.handler;

import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;
import com.relaxed.extend.dingtalk.message.DingTalkTextMessage;
import com.relaxed.extend.dingtalk.request.DingTalkResponse;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
public class DingTalkGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final DingTalkSender sender;

	public DingTalkGlobalExceptionHandler(ExceptionHandleConfig config, DingTalkSender sender, String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		DingTalkResponse response = sender
				.sendMessage(new DingTalkTextMessage().setContent(sendMessage.toString()).atAll());
		return new ExceptionNoticeResponse().setErrMsg(response.getResponse()).setSuccess(response.isSuccess());
	}

}
