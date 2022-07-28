package com.relaxed.oauth2.resource.configuration;

import cn.hutool.core.util.ArrayUtil;
import com.relaxed.oauth2.common.constant.SecurityConstant;
import com.relaxed.oauth2.resource.RemoteTokenServiceProvider;
import com.relaxed.oauth2.resource.properties.ExtendResourceServerProperties;
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
 * @author Yakir
 * @Topic CustomResourceServerConfigurer
 * @Description
 * @date 2022/7/25 15:06
 * @Version 1.0
 */
@RequiredArgsConstructor
public class CustomResourceServerConfigurer extends ResourceServerConfigurerAdapter {

	private final AuthenticationEntryPoint authenticationEntryPoint;

	private final AccessDeniedHandler accessDeniedHandler;

	private final ResourceServerProperties resource;

	private final ExtendResourceServerProperties extendResource;

	private RemoteTokenServiceProvider remoteTokenServiceProvider;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(extendResource.getResourceId());
		resources.authenticationEntryPoint(authenticationEntryPoint);
		resources.accessDeniedHandler(accessDeniedHandler);
		if (remoteTokenServiceProvider != null) {
			ResourceServerTokenServices tokenServices = remoteTokenServiceProvider.provide(resource);
			resources.tokenServices(tokenServices);
		}
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		List<String> ignoreUrls = extendResource.getIgnoreUrls();
		ignoreUrls.addAll(SecurityConstant.DEFAULT_IGNORE_AUTH_URL);
		http.authorizeRequests().antMatchers(ArrayUtil.toArray(ignoreUrls, String.class)).permitAll().anyRequest()
				.authenticated()
				// 关闭 csrf 跨站攻击防护
				.and().csrf().disable();
	}

	@Autowired(required = false)
	public void setRemoteTokenServiceProvider(RemoteTokenServiceProvider remoteTokenServiceProvider) {
		this.remoteTokenServiceProvider = remoteTokenServiceProvider;
	}

}
