package com.relaxed.oauth2.auth.configurer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.List;

/**
 * 自定义Web安全配置器 扩展Spring Security的Web安全配置 支持自定义认证提供者和安全规则
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	/**
	 * 认证提供者列表 用于支持多种认证方式
	 */
	private final List<AuthenticationProvider> authenticationProviderList;

	/**
	 * 授权端点路径 用于OAuth2授权请求
	 */
	private static final String AUTHORIZE_ENDPOINT_PATH = "/oauth/authorize";

	/**
	 * 配置认证管理器Bean 用于处理认证请求
	 * @return 认证管理器
	 * @throws Exception 当配置过程中发生错误时抛出
	 */
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 配置认证管理器 注册所有自定义的认证提供者
	 * @param auth 认证管理器构建器
	 * @throws Exception 当配置过程中发生错误时抛出
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (CollectionUtil.isEmpty(authenticationProviderList)) {
			return;
		}
		for (AuthenticationProvider authenticationProvider : authenticationProviderList) {
			auth.authenticationProvider(authenticationProvider);
		}
	}

	/**
	 * 配置HTTP安全规则 设置基本的安全配置，包括： 1. 禁用CSRF保护 2. 启用表单登录 3. 配置授权端点的访问规则
	 * @param http HTTP安全配置器
	 * @throws Exception 当配置过程中发生错误时抛出
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().formLogin().and().authorizeRequests().antMatchers(AUTHORIZE_ENDPOINT_PATH)
				.authenticated();
	}

}
