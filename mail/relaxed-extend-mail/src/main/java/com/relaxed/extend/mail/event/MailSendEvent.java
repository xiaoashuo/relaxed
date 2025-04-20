package com.relaxed.extend.mail.event;

import com.relaxed.extend.mail.model.MailSendInfo;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 邮件发送事件类，用于在邮件发送完成后发布事件。 继承自Spring的ApplicationEvent，用于事件驱动编程。
 *
 * @author Hccake
 * @since 1.0
 */
@ToString
public class MailSendEvent extends ApplicationEvent {

	/**
	 * 创建邮件发送事件
	 * @param mailSendInfo 邮件发送结果信息
	 */
	public MailSendEvent(MailSendInfo mailSendInfo) {
		super(mailSendInfo);
	}

}
