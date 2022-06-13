package com.relaxed.starter.validate.code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yakir
 * @Topic ValidateCodeProperties
 * @Description
 * @date 2022/6/12 15:57
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "relaxed.validate.code")
public class ValidateCodeProperties {

	/**
	 * 验证码存储类型 LOCAL,REDIS
	 */
	private String storageType;

	/**
	 * 渠道
	 */
	private Map<String, Boolean> channels = new HashMap<>();

}
