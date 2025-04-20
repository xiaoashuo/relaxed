package com.relaxed.oauth2.auth.builder;

import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;

/**
 * Token授予者提供者接口 用于自定义Token授予者的创建逻辑 支持扩展OAuth2的授权类型和Token授予方式
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface TokenGranterProvider {

	/**
	 * 提供Token授予者 根据授权服务器端点配置创建Token授予者
	 * @param endpoints 授权服务器端点配置器
	 * @return Token授予者实例
	 */
	TokenGranter provide(final AuthorizationServerEndpointsConfigurer endpoints);

}
