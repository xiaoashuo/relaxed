package com.relaxed.autoconfigure.web.exception;

import cn.hutool.core.bean.BeanUtil;
import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.aop.*;
import com.relaxed.common.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.exception.handler.DingTalkGlobalExceptionHandler;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.exception.handler.MailGlobalExceptionHandler;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import com.relaxed.extend.mail.sender.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author Yakir
 * @Topic ExceptionAutoConfiguration
 * @Description
 * @date 2021/12/21 10:12
 * @Version 1.0
 */
@EnableConfigurationProperties(ExceptionHandleProperties.class)
@Configuration(proxyBeanMethods = false)
public class ExceptionAutoConfiguration {

	@Value("${spring.application.name}")
	private String applicationName;

	/**
	 * 钉钉消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "relaxed.exception", name = "type", havingValue = "DING_TALK")
	public GlobalExceptionHandler dingTalkGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		ExceptionHandleConfig exceptionHandleConfig = BeanUtil.toBean(exceptionHandleProperties,
				ExceptionHandleConfig.class);
		return new DingTalkGlobalExceptionHandler(exceptionHandleConfig, context.getBean(DingTalkSender.class),
				applicationName);
	}

	/**
	 * 邮件消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	@ConditionalOnProperty(prefix = "relaxed.exception", name = "type", havingValue = "MAIL")
	public GlobalExceptionHandler mailGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		ExceptionHandleConfig exceptionHandleConfig = BeanUtil.toBean(exceptionHandleProperties,
				ExceptionHandleConfig.class);
		return new MailGlobalExceptionHandler(exceptionHandleConfig, context.getBean(MailSender.class), applicationName,
				exceptionHandleProperties.getReceiveEmails());
	}

}
