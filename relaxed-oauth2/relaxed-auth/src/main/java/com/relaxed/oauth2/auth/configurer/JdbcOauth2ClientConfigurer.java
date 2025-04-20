package com.relaxed.oauth2.auth.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import javax.sql.DataSource;

/**
 * JDBC OAuth2客户端配置器 实现基于数据库的OAuth2客户端配置 使用JDBC方式存储和管理客户端信息
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class JdbcOauth2ClientConfigurer implements OAuth2ClientConfigurer {

	/**
	 * 数据源 用于访问存储客户端信息的数据库
	 */
	private final DataSource dataSource;

	/**
	 * 配置客户端详情服务 使用JDBC方式配置客户端信息的存储
	 * @param configurer 客户端详情服务配置器
	 * @throws Exception 当配置过程中发生错误时抛出
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.jdbc(dataSource);
	}

}
