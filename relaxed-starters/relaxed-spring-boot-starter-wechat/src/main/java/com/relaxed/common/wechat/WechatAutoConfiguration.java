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
 * Wechat 自动配置
 *
 * @author Yakir
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "relaxed.wechat.url")
@EnableConfigurationProperties({ WechatProperties.class })
public class WechatAutoConfiguration {

	private final WechatProperties wechatProperties;

	@Bean
	@ConditionalOnMissingBean
	public WechatSender wechatSender() {
		return new WechatSender(wechatProperties.getUrl());
	}

}
