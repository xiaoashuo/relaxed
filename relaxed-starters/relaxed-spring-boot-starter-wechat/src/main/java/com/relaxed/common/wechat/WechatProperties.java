package com.relaxed.common.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 微信配置属性类 用于配置微信通知相关的属性
 *
 * @author lingting
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.wechat")
public class WechatProperties {

	/**
	 * 企业微信Webhook地址 用于接收通知消息的URL
	 */
	private String url;

}
