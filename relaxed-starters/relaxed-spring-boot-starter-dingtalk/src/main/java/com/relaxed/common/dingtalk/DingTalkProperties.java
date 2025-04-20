package com.relaxed.common.dingtalk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 钉钉配置属性类 用于配置钉钉通知相关的属性
 *
 * @author lingting
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.dingtalk")
public class DingTalkProperties {

	/**
	 * 钉钉Webhook地址 用于接收通知消息的URL
	 */
	private String url;

	/**
	 * 钉钉机器人密钥 用于消息签名的安全密钥
	 */
	private String secret;

}
