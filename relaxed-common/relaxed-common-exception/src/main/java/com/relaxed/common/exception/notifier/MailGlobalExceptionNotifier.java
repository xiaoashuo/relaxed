package com.relaxed.common.exception.notifier;

import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResult;
import com.relaxed.extend.mail.model.MailSendInfo;
import com.relaxed.extend.mail.sender.MailSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 异常邮件通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
@Getter
public class MailGlobalExceptionNotifier extends AbstractExceptionNotifier {

	private final MailSender sender;

	private String[] receiveEmails;

	public MailGlobalExceptionNotifier(String channel, String applicationName, MailSender sender,
			Set<String> receiveEmails) {
		super(channel, applicationName);
		this.sender = sender;
		this.receiveEmails = receiveEmails.toArray(new String[0]);
	}

	@Override
	public ExceptionNoticeResult send(ExceptionMessage sendMessage) {
		MailSendInfo mailSendInfo = sender.sendTextMail("异常警告[应用:" + applicationName + "]", sendMessage.toString(),
				receiveEmails);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResult().setChannel(channel).setSuccess(mailSendInfo.getSuccess())
				.setErrMsg(mailSendInfo.getErrorMsg());
	}

}
