package com.relaxed.common.exception.handler;

import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.domain.ExceptionMessage;
import com.relaxed.common.exception.domain.ExceptionNoticeResponse;
import com.relaxed.extend.mail.model.MailSendInfo;
import com.relaxed.extend.mail.sender.MailSender;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * 异常邮件通知
 *
 * @author lingting 2020/6/12 0:25
 */
@Slf4j
public class MailGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final MailSender sender;

	private String[] receiveEmails;

	public MailGlobalExceptionHandler(ExceptionHandleConfig config, MailSender sender, String applicationName,
			Set<String> receiveEmails) {
		super(config, applicationName);
		this.sender = sender;
		this.receiveEmails = receiveEmails.toArray(new String[0]);
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		MailSendInfo mailSendInfo = sender.sendTextMail("异常警告", sendMessage.toString(), receiveEmails);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResponse().setSuccess(mailSendInfo.getSuccess())
				.setErrMsg(mailSendInfo.getErrorMsg());
	}

}
