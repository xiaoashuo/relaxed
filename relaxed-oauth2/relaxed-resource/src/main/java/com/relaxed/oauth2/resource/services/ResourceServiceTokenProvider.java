package com.relaxed.oauth2.resource.services;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * 资源服务Token提供者接口 用于提供资源服务器的Token服务 支持自定义Token服务的实现
 *
 * @author Yakir
 * @since 1.0
 */
public interface ResourceServiceTokenProvider {

	/**
	 * 提供资源服务器的Token服务 根据资源服务器配置创建Token服务
	 * @param resourceServerProperties 资源服务器配置
	 * @return Token服务实例
	 */
	ResourceServerTokenServices provide(ResourceServerProperties resourceServerProperties);

}
