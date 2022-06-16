package com.relaxed.autoconfigure.exception;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.relaxed.common.exception.ExceptionHandleConfig;
import com.relaxed.common.exception.handler.DefaultGlobalExceptionHandler;
import com.relaxed.common.exception.handler.GlobalExceptionHandler;
import com.relaxed.common.exception.holder.ExceptionNotifierHolder;
import com.relaxed.common.exception.notifier.*;
import com.relaxed.extend.dingtalk.request.DingTalkSender;
import com.relaxed.extend.mail.sender.MailSender;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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

	private static final String DING_TALK = "DING_TALK";

	private static final String MAIL = "MAIL";

	@Value("${spring.application.name: unknown-application}")
	private String applicationName;

	/**
	 * 通知结果 决策器 判断是否成功 默认任何一个通知器通知 成功即为本次通知成功
	 * @author yakir
	 * @date 2022/1/20 13:38
	 * @return com.relaxed.common.exception.notifier.NoticeResultDecision
	 */
	@Bean
	@ConditionalOnMissingBean
	public NoticeResultDecision noticeResultDecision() {
		return new DefaultNoticeResultDecision();
	}

	/**
	 * 异常通知者持有器
	 * @author yakir
	 * @date 2022/1/20 11:50
	 * @param exceptionNotifiers
	 * @return com.relaxed.common.exception.holder.ExceptionNotifierHolder
	 */
	@Bean
	@ConditionalOnMissingBean
	public ExceptionNotifierHolder exceptionNotifierHolder(ObjectProvider<ExceptionNotifier> exceptionNotifiers,
			NoticeResultDecision noticeResultDecision) {
		List<ExceptionNotifier> notifiers = new ArrayList<>();
		exceptionNotifiers.orderedStream().iterator().forEachRemaining(notifiers::add);
		if (CollectionUtil.isEmpty(notifiers)) {
			// 若异常通知处理器为空 则填入默认异常处理器
			notifiers.add(new DefaultGlobalExceptionNotifier());
		}
		return new ExceptionNotifierHolder(notifiers, noticeResultDecision);
	}

	/**
	 * 全局异常处理器
	 * @author yakir
	 * @date 2022/1/20 11:53
	 * @param exceptionHandleProperties
	 * @param exceptionNotifierHolder
	 * @return com.relaxed.common.exception.handler.GlobalExceptionHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public GlobalExceptionHandler defaultGlobalExceptionHandler(ExceptionHandleProperties exceptionHandleProperties,
			ExceptionNotifierHolder exceptionNotifierHolder) {
		ExceptionHandleConfig exceptionHandleConfig = BeanUtil.toBean(exceptionHandleProperties,
				ExceptionHandleConfig.class);
		return new DefaultGlobalExceptionHandler(exceptionHandleConfig, exceptionNotifierHolder, applicationName);
	}

	/**
	 * 钉钉消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.exception.channels", name = DING_TALK, havingValue = "true")
	public ExceptionNotifier dingTalkGlobalExceptionNotifier(ApplicationContext context) {
		return new DingTalkGlobalExceptionNotifier(DING_TALK, applicationName, context.getBean(DingTalkSender.class));
	}

	/**
	 * 邮件消息通知的日志处理器
	 *
	 * @author lingting 2020-06-12 00:32:40
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.exception.channels", name = MAIL, havingValue = "true")
	public ExceptionNotifier mailGlobalExceptionNotifier(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		return new MailGlobalExceptionNotifier(MAIL, applicationName, context.getBean(MailSender.class),
				exceptionHandleProperties.getReceiveEmails());
	}

}
