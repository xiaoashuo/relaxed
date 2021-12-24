package com.relaxed.autoconfigure.exception;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.annotation.ExceptionNotice;
import com.relaxed.common.exception.aop.AnnotationMethodPoint;
import com.relaxed.common.exception.aop.PointCutRegister;
import com.relaxed.common.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.exception.handler.DingTalkGlobalExceptionHandler;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.exception.handler.MailGlobalExceptionHandler;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import com.relaxed.extend.mail.sender.MailSender;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	 * 默认的异常处理器
	 * @return DefaultExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean(GlobalExceptionHandler.class)
	public GlobalExceptionHandler defaultGlobalExceptionHandler() {
		return new DefaultGlobalExceptionHandler();
	}

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
		String appName = chooseAppName(exceptionHandleProperties);
		return new DingTalkGlobalExceptionHandler(exceptionHandleConfig, context.getBean(DingTalkSender.class),
				appName);
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
		String appName = chooseAppName(exceptionHandleProperties);
		return new MailGlobalExceptionHandler(exceptionHandleConfig, context.getBean(MailSender.class), appName,
				exceptionHandleProperties.getReceiveEmails());
	}

	private String chooseAppName(ExceptionHandleProperties exceptionHandleProperties) {
		String appNameProperties = exceptionHandleProperties.getAppName();
		return StrUtil.isEmpty(appNameProperties) ? applicationName : appNameProperties;
	}

}
