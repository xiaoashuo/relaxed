package com.relaxed.common.security.jwt.config;

import com.relaxed.common.security.jwt.core.JwtTokenService;
import com.relaxed.common.security.jwt.handler.DefaultAuthenticationEntryPoint;
import com.relaxed.common.security.jwt.provider.JwtAuthenticationProvider;
import com.relaxed.common.security.jwt.tookit.CustomPermissionEvaluator;
import com.relaxed.common.security.jwt.tookit.IPermissionEvaluator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Yakir
 * @Topic JwtConfiguration
 * @Description
 * @date 2021/8/18 14:55
 * @Version 1.0
 */
@Configuration
public class JwtSecurityConfiguration {

	/**
	 * 认证失败处理类
	 * @author yakir
	 * @date 2021/8/18 15:54
	 * @return org.springframework.security.web.AuthenticationEntryPoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new DefaultAuthenticationEntryPoint();
	}

	/**
	 * dao 认证提供者
	 * @param userDetailsService
	 * @param passwordEncoder
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}

	/**
	 * jwt 认证提供者
	 * @author yakir
	 * @date 2021/8/18 15:11
	 * @param userDetailsService
	 * @param jwtTokenService
	 * @return com.relaxed.common.security.jwt.provider.JwtAuthenticationProvider
	 */
	@Bean
	@ConditionalOnMissingBean
	public JwtAuthenticationProvider jwtAuthenticationProvider(UserDetailsService userDetailsService,
			JwtTokenService jwtTokenService) {
		return new JwtAuthenticationProvider(userDetailsService, jwtTokenService);
	}

	/**
	 * 密码编码器
	 * @author yakir
	 * @date 2021/8/18 15:02
	 * @return org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
	 */
	@Bean
	@ConditionalOnMissingBean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 自定义权限判断器
	 * @author yakir
	 * @date 2021/8/18 15:04
	 * @return com.relaxed.common.security.jwt.tookit.IPermissionEvaluator
	 */
	@Bean(name = "pre")
	@ConditionalOnMissingBean
	public IPermissionEvaluator iPermissionEvaluator() {
		return new CustomPermissionEvaluator();
	}

}
