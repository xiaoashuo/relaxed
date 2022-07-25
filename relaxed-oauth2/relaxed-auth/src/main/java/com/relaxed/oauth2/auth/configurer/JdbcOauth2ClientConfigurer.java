package com.relaxed.oauth2.auth.configurer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

import javax.sql.DataSource;

/**
 * @author Yakir
 * @Topic JdbcOauth2ClientConfigurer
 * @Description
 * @date 2022/7/24 14:04
 * @Version 1.0
 */
@RequiredArgsConstructor
public class JdbcOauth2ClientConfigurer implements OAuth2ClientConfigurer {

	private final DataSource dataSource;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		configurer.jdbc(dataSource);
	}

}
