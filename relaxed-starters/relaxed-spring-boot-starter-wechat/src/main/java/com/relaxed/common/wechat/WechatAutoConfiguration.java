package com.relaxed.common.wechat;

import com.relaxed.extend.wechat.request.WechatSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信自动配置类 提供企业微信通知的自动配置功能
 *
 * @author Yakir
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "relaxed.wechat.url")
@EnableConfigurationProperties({ WechatProperties.class })
public class WechatAutoConfiguration {

	private final WechatProperties wechatProperties;

	/**
	 * 创建企业微信消息发送器 当配置了企业微信Webhook地址时启用
	 * @return WechatSender 企业微信消息发送器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public WechatSender wechatSender() {
		return new WechatSender(wechatProperties.getUrl());
	}

}
