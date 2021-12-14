package com.relaxed.autoconfigure.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/11 14:56
 */
@Data
@ConfigurationProperties(prefix = "relaxed.log.access")
public class AccessLogProperties {

	/**
	 * 忽略的Url匹配规则，Ant风格
	 */
	private List<String> ignoreUrlPatterns = Arrays.asList("/actuator/**", "/webjars/**", "/favicon.ico",
			"/captcha/get");

}
