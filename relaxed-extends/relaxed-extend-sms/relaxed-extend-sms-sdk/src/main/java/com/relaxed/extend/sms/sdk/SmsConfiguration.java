package com.relaxed.extend.sms.sdk;

import com.relaxed.extend.sms.sdk.core.SmsProperties;
import com.relaxed.extend.sms.sdk.handler.DefaultIEncryptionHandler;
import com.relaxed.extend.sms.sdk.handler.IEncryptionHandler;
import com.relaxed.extend.sms.sdk.service.impl.SmsSenderImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yakir
 * @Topic SmsConfiguration
 * @Description
 * @date 2021/8/26 15:41
 * @Version 1.0
 */
@EnableConfigurationProperties(SmsProperties.class)
@Configuration(proxyBeanMethods = false)
public class SmsConfiguration {

	@Bean
	public IEncryptionHandler encryptionHandler() {
		return new DefaultIEncryptionHandler();
	}

	@Bean
	public SmsProperties smsProperties() {
		return new SmsProperties();
	}

	@Bean
	public SmsSender smsSender(SmsProperties smsProperties, IEncryptionHandler encryptionHandler) {
		return new SmsSenderImpl(smsProperties, encryptionHandler);
	}

}
