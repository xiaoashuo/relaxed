package com.relaxed.samples.controller;

import com.relaxed.common.mail.model.MailSendInfo;
import com.relaxed.common.mail.sender.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yakir
 * @Topic MailController
 * @Description
 * @date 2021/6/21 11:41
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("mail")
public class MailController {

	private final MailSender mailSender;

	/**
	 * 发送文本消息
	 * @param msg
	 * @return
	 */
	@GetMapping("/text/send")
	public MailSendInfo sendSimpleMsg(String msg) {
		MailSendInfo sendInfo = mailSender.sendTextMail("test", msg, "1412844668@qq.com");
		return sendInfo;
	}

}
