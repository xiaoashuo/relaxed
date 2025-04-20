package com.relaxed.oauth2.resource.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展资源服务器配置属性 用于配置OAuth2资源服务器的扩展属性 支持配置资源ID和忽略鉴权的URL列表
 *
 * @author Yakir
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.extension")
public class ExtendResourceServerProperties {

	/**
	 * 资源服务的ID 用于标识资源服务器，可选配置 如果不配置，将使用默认值
	 */
	private String resourceId;

	/**
	 * 忽略鉴权的URL列表 配置需要跳过OAuth2鉴权的URL路径 支持Ant风格的路径匹配
	 */
	private List<String> ignoreUrls = new ArrayList<>();

}
