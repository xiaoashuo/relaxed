package com.relaxed.extend.mail.event;

import com.relaxed.extend.mail.model.MailSendInfo;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 18:00
 */
@ToString
public class MailSendEvent extends ApplicationEvent {

	public MailSendEvent(MailSendInfo mailSendInfo) {
		super(mailSendInfo);
	}

}
