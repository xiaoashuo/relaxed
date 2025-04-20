package com.relaxed.oauth2.auth.configurer;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

/**
 * OAuth2客户端配置器接口 用于自定义OAuth2授权服务器的客户端配置 实现此接口可以自定义客户端信息的存储方式，如数据库、内存等
 *
 * @author hccake
 * @since 1.0
 */
public interface OAuth2ClientConfigurer {

	/**
	 * 配置客户端详情服务 用于设置客户端信息的存储方式和配置
	 * @param clientDetailsServiceConfigurer 客户端详情服务配置器
	 * @throws Exception 当配置过程中发生错误时抛出
	 */
	void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception;

}
