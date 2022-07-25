package com.relaxed.oauth2.auth.configurer;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic CustomWebSecurityConfigurer
 * @Description
 * @date 2022/7/24 14:58
 * @Version 1.0
 */

@RequiredArgsConstructor
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	private final List<AuthenticationProvider> authenticationProviderList;

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (CollectionUtil.isEmpty(authenticationProviderList)) {
			return;

		}
		for (AuthenticationProvider authenticationProvider : authenticationProviderList) {
			auth.authenticationProvider(authenticationProvider);
		}

	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/oauth/**", "/login/**", "/logout/**").permitAll()
				.anyRequest().authenticated().and().formLogin().permitAll();
	}

}
