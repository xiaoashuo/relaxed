package com.relaxed.common.dingtalk;

import com.relaxed.extend.dingtalk.request.DingTalkSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉自动配置类 提供钉钉通知的自动配置功能
 *
 * @author lingting
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "relaxed.dingtalk.url")
@EnableConfigurationProperties({ DingTalkProperties.class })
public class DingTalkAutoConfiguration {

	private final DingTalkProperties dingTalkProperties;

	/**
	 * 创建钉钉消息发送器 当配置了钉钉Webhook地址时启用
	 * @return DingTalkSender 钉钉消息发送器实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public DingTalkSender dingTalkSender() {
		return new DingTalkSender(dingTalkProperties.getUrl()).setSecret(dingTalkProperties.getSecret());
	}

}
