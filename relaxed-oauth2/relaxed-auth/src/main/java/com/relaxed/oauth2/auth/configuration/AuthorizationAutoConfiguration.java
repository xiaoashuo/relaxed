package com.relaxed.oauth2.auth.configuration;

import com.relaxed.oauth2.auth.builder.TokenGrantBuilder;
import com.relaxed.oauth2.auth.builder.TokenGranterProvider;
import com.relaxed.oauth2.auth.configurer.CustomAuthorizationServerConfigurer;
import com.relaxed.oauth2.auth.configurer.CustomWebSecurityConfigurer;
import com.relaxed.oauth2.auth.configurer.JdbcOauth2ClientConfigurer;
import com.relaxed.oauth2.auth.configurer.OAuth2ClientConfigurer;
import com.relaxed.oauth2.auth.exception.CustomWebResponseExceptionTranslator;
import com.relaxed.oauth2.auth.extension.captcha.CaptchaTokenGranter;
import com.relaxed.oauth2.auth.extension.captcha.CaptchaValidator;

import com.relaxed.oauth2.auth.extension.mobile.SmsCodeAuthenticationProvider;
import com.relaxed.oauth2.auth.extension.mobile.SmsCodeValidator;
import com.relaxed.oauth2.auth.util.PasswordUtils;
import com.relaxed.oauth2.common.handler.CustomAccessDeniedHandler;
import com.relaxed.oauth2.common.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yakir
 * @Topic AuthorizationAutoConfiguration
 * @Description
 * @date 2022/7/24 11:45
 * @Version 1.0
 */
@Import({ CustomWebSecurityConfigurer.class, CustomAuthorizationServerConfigurer.class })
public class AuthorizationAutoConfiguration {

	/**
	 * 令牌存储
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	/**
	 * 定义access token转换器 将token字符串转换成OAuth2AccessToken对象
	 * @author yakir
	 * @date 2022/7/24 13:25
	 * @return org.springframework.security.oauth2.provider.token.AccessTokenConverter
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessTokenConverter accessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}

	/**
	 * 自定义的认证时异常转换
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebResponseExceptionTranslator customWebResponseExceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	/**
	 * 密码解析器，只在授权服务器中进行配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return PasswordUtils.ENCODER;
	}

	/**
	 * 自定义异常处理
	 * @return AuthenticationEntryPoint
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	/**
	 * 进入异常处理
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	/**
	 * OAuth2 客户端配置类，默认使用 jdbc 从数据库获取 OAuth2 Client 信息
	 * @return JdbcOAuth2ClientConfigurer
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2ClientConfigurer oAuth2ClientConfigurer(DataSource dataSource) {
		return new JdbcOauth2ClientConfigurer(dataSource);
	}

	/**
	 * 授权类型建造者，默认处理了 OAuth2 规范的 5 种授权类型，用户可自定义添加其他授权类型，如手机号登录
	 * @param authenticationManager 认证管理器
	 * @return TokenGrantBuilder
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenGrantBuilder tokenGrantBuilder(AuthenticationManager authenticationManager,
			@Autowired(required = false) CaptchaValidator captchaValidator) {

		// 添加验证码授权模式授权者
		TokenGranterProvider captchaTokenGranter = endpoints -> new CaptchaTokenGranter(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager,
				captchaValidator);
		List<TokenGranterProvider> tokenGranterProviderList = new ArrayList<>();
		tokenGranterProviderList.add(captchaTokenGranter);
		TokenGrantBuilder tokenGrantBuilder = new TokenGrantBuilder(authenticationManager, tokenGranterProviderList);
		return tokenGrantBuilder;
	}

	/**
	 * 用户名密码认证授权提供者
	 * @return
	 */
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	/**
	 * 手机验证码认证授权提供者
	 * @return
	 */
	@Bean
	public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider(UserDetailsService userDetailsService,
			@Autowired(required = false) SmsCodeValidator smsCodeValidator) {
		SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider(smsCodeValidator,
				userDetailsService);
		return provider;
	}

}
