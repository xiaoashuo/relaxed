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

import com.relaxed.extend.wechat.request.WechatSender;
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
 * 异常自动配置类 提供全局异常处理、通知渠道配置等功能
 *
 * @author Yakir
 * @since 1.0
 */
@EnableConfigurationProperties(ExceptionHandleProperties.class)
@Configuration(proxyBeanMethods = false)
public class ExceptionAutoConfiguration {

	private static final String DING_TALK = "DING_TALK";

	private static final String MAIL = "MAIL";

	private static final String WECHAT = "WECHAT";

	@Value("${spring.application.name: unknown-application}")
	private String applicationName;

	/**
	 * 创建通知结果决策器 用于判断通知是否成功，默认任何一个通知器通知成功即为本次通知成功
	 * @return NoticeResultDecision 通知结果决策器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public NoticeResultDecision noticeResultDecision() {
		return new DefaultNoticeResultDecision();
	}

	/**
	 * 创建异常通知者持有器 用于管理所有异常通知器，如果没有配置任何通知器，则使用默认通知器
	 * @param exceptionNotifiers 异常通知器提供者
	 * @param noticeResultDecision 通知结果决策器
	 * @return ExceptionNotifierHolder 异常通知者持有器实例
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
	 * 创建全局异常处理器 用于处理应用程序中的全局异常
	 * @param exceptionHandleProperties 异常处理配置属性
	 * @param exceptionNotifierHolder 异常通知者持有器
	 * @return GlobalExceptionHandler 全局异常处理器实例
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
	 * 创建微信消息通知的异常处理器 当配置了微信通知渠道时启用
	 * @param context Spring应用上下文
	 * @return ExceptionNotifier 微信异常通知器实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.exception.channels", name = WECHAT, havingValue = "true")
	public ExceptionNotifier wechatGlobalExceptionNotifier(ApplicationContext context) {
		return new WechatGlobalExceptionNotifier(WECHAT, applicationName, context.getBean(WechatSender.class));
	}

	/**
	 * 创建钉钉消息通知的异常处理器 当配置了钉钉通知渠道时启用
	 * @param context Spring应用上下文
	 * @return ExceptionNotifier 钉钉异常通知器实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.exception.channels", name = DING_TALK, havingValue = "true")
	public ExceptionNotifier dingTalkGlobalExceptionNotifier(ApplicationContext context) {
		return new DingTalkGlobalExceptionNotifier(DING_TALK, applicationName, context.getBean(DingTalkSender.class));
	}

	/**
	 * 创建邮件消息通知的异常处理器 当配置了邮件通知渠道时启用
	 * @param exceptionHandleProperties 异常处理配置属性
	 * @param context Spring应用上下文
	 * @return ExceptionNotifier 邮件异常通知器实例
	 */
	@Bean
	@ConditionalOnProperty(prefix = "relaxed.exception.channels", name = MAIL, havingValue = "true")
	public ExceptionNotifier mailGlobalExceptionNotifier(ExceptionHandleProperties exceptionHandleProperties,
			ApplicationContext context) {
		return new MailGlobalExceptionNotifier(MAIL, applicationName, context.getBean(MailSender.class),
				exceptionHandleProperties.getReceiveEmails());
	}

}
