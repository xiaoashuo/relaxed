package com.relaxed.oauth2.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic Oauth2ResourceServerProperties
 * @Description
 * @date 2022/7/25 15:05
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.extension")
public class ExtendResourceServerProperties {

	/**
	 * 资源服务的ID(Optional)
	 */
	private String resourceId;

	/**
	 * 忽略鉴权的 url 列表
	 */
	private List<String> ignoreUrls = new ArrayList<>();

}
