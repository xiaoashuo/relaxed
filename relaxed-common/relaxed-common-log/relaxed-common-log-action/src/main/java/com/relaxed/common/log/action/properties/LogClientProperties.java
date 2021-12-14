package com.relaxed.common.log.action.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic LogClientProperties
 * @Description
 * @date 2021/12/14 14:15
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "relaxed.log.client")
@Data
public class LogClientProperties {

	/**
	 * 应用名称
	 */
	private String appName;

}
