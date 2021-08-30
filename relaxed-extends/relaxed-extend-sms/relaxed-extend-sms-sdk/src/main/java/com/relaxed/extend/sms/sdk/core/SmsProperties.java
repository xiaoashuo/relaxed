package com.relaxed.extend.sms.sdk.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic SmsProperties
 * @Description
 * @date 2021/8/26 15:16
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.sms")
public class SmsProperties {

	/**
	 * 是否开启鉴权
	 */
	private boolean auth;

	/**
	 * 主机
	 */
	private String host;

	/**
	 * 进入key
	 */
	private String accessKey;

	/**
	 * 进入密钥
	 */
	private String accessSecret;

}
