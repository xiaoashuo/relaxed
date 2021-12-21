package com.relaxed.common.mail;

import com.relaxed.extend.mail.sender.MailSender;
import com.relaxed.extend.mail.sender.MailSenderImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author Hccake 2021/1/7
 * @version 1.0
 */
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class MailAutoConfiguration {

	@Bean
	@ConditionalOnBean(JavaMailSender.class)
	@ConditionalOnMissingBean(MailSender.class)
	public MailSender mailSenderImpl(JavaMailSender javaMailSender,
			ApplicationEventPublisher applicationEventPublisher) {
		return new MailSenderImpl(javaMailSender, applicationEventPublisher);
	}

}
