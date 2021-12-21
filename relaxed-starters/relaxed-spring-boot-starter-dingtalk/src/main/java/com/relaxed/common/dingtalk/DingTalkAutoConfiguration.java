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
 * @author lingting 2020/6/11 23:14
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "relaxed.dingtalk.url")
@EnableConfigurationProperties({ DingTalkProperties.class })
public class DingTalkAutoConfiguration {

	private final DingTalkProperties dingTalkProperties;

	@Bean
	@ConditionalOnMissingBean
	public DingTalkSender dingTalkSender() {
		return new DingTalkSender(dingTalkProperties.getUrl()).setSecret(dingTalkProperties.getSecret());
	}

}
