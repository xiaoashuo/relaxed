package com.relaxed.oauth2.resource.configuration;

import cn.hutool.core.util.ArrayUtil;
import com.relaxed.oauth2.common.constant.SecurityConstant;

import com.relaxed.oauth2.resource.properties.ExtendResourceServerProperties;
import com.relaxed.oauth2.resource.services.ResourceServiceTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.List;

/**
 * 自定义资源服务器配置器 用于配置OAuth2资源服务器的安全设置 支持配置资源ID、认证入口点、访问拒绝处理器和Token服务 支持配置忽略鉴权的URL列表
 *
 * @author Yakir
 * @since 1.0
 */
@RequiredArgsConstructor
public class CustomResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	/**
	 * 认证入口点，用于处理认证失败的情况
	 */
	private final AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 访问拒绝处理器，用于处理权限不足的情况
	 */
	private final AccessDeniedHandler accessDeniedHandler;

	/**
	 * 扩展资源服务器配置属性
	 */
	private final ExtendResourceServerProperties extendResource;

	/**
	 * 资源服务器配置属性
	 */
	private final ResourceServerProperties resourceServerProperties;

	/**
	 * 资源服务Token提供者
	 */
	private ResourceServiceTokenProvider resourceServiceTokenProvider;

	/**
	 * 配置资源服务器安全设置 设置资源ID、认证入口点、访问拒绝处理器和Token服务
	 * @param resources 资源服务器安全配置器
	 * @throws Exception 配置异常
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(extendResource.getResourceId());
		resources.authenticationEntryPoint(authenticationEntryPoint);
		resources.accessDeniedHandler(accessDeniedHandler);
		if (resourceServiceTokenProvider != null) {
			ResourceServerTokenServices tokenServices = resourceServiceTokenProvider.provide(resourceServerProperties);
			resources.tokenServices(tokenServices);
		}
	}

	/**
	 * 配置HTTP安全设置 设置忽略鉴权的URL列表和CSRF防护
	 * @param http HTTP安全配置器
	 * @throws Exception 配置异常
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		List<String> ignoreUrls = extendResource.getIgnoreUrls();
		ignoreUrls.addAll(SecurityConstant.DEFAULT_IGNORE_AUTH_URL);
		http.authorizeRequests().antMatchers(ArrayUtil.toArray(ignoreUrls, String.class)).permitAll().anyRequest()
				.authenticated()
				// 关闭 csrf 跨站攻击防护
				.and().csrf().disable();
	}

	/**
	 * 设置资源服务Token提供者 用于提供自定义的Token服务
	 * @param remoteTokenServiceProvider 资源服务Token提供者
	 */
	@Autowired(required = false)
	public void setRemoteTokenServiceProvider(ResourceServiceTokenProvider remoteTokenServiceProvider) {
		this.resourceServiceTokenProvider = remoteTokenServiceProvider;
	}

}
