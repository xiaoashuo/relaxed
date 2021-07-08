package com.relaxed.common.swagger.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Yakir
 * @Topic SwaggerProviderProperties
 * @Description
 * @date 2021/7/8 14:00
 * @Version 1.0
 */
@Data
@ConfigurationProperties("relaxed.swagger.provider")
public class SwaggerProviderProperties {

	/**
	 * 聚合者的来源，用于控制跨域放行
	 */
	private String aggregatorOrigin = "*";

}
