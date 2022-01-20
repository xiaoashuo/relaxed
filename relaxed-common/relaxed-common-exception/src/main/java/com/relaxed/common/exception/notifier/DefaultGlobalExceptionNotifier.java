package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 钉钉消息通知
 *
 * @author lingting 2020/6/12 0:25
 */
@RequiredArgsConstructor
@Getter
@Slf4j
public class DefaultGlobalExceptionNotifier implements ExceptionNotifier {

	private final String channel;

	public DefaultGlobalExceptionNotifier() {
		this.channel = "DEFAULT";
	}

	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		log.error("默认异常通知,消息{}", sendMessage);
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(true);
	}

}
