package com.relaxed.oauth2.auth.configuration;

import com.relaxed.oauth2.auth.builder.TokenGrantBuilder;
import com.relaxed.oauth2.auth.builder.TokenGranterProvider;
import com.relaxed.oauth2.auth.builder.TokenServicesBuilder;
import com.relaxed.oauth2.auth.configurer.CustomAuthorizationServerConfigurer;
import com.relaxed.oauth2.auth.configurer.CustomWebSecurityConfigurer;
import com.relaxed.oauth2.auth.configurer.JdbcOauth2ClientConfigurer;
import com.relaxed.oauth2.auth.configurer.OAuth2ClientConfigurer;
import com.relaxed.oauth2.auth.exception.CustomWebResponseExceptionTranslator;
import com.relaxed.oauth2.auth.extension.ExtendUserDetailsService;
import com.relaxed.oauth2.auth.extension.PreValidator;
import com.relaxed.oauth2.auth.extension.PreValidatorHolder;
import com.relaxed.oauth2.auth.extension.captcha.CaptchaTokenGranter;

import com.relaxed.oauth2.auth.extension.mobile.SmsCodeTokenGranter;
import com.relaxed.oauth2.auth.functions.RetriveUserFunction;
import com.relaxed.oauth2.auth.handler.AuthorizationInfoHandle;
import com.relaxed.oauth2.auth.extension.mobile.SmsCodeAuthenticationProvider;

import com.relaxed.oauth2.auth.util.PasswordUtils;
import com.relaxed.oauth2.common.handler.CustomAccessDeniedHandler;
import com.relaxed.oauth2.common.handler.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.*;
import java.util.stream.Collectors;

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
	 * 预检查持有器
	 * @param preValidatorProvider
	 * @return
	 */
	@Bean
	public PreValidatorHolder preValidatorHolder(ObjectProvider<PreValidator> preValidatorProvider) {
		Map<String, PreValidator> preValidatorMap = new HashMap<>(8);
		preValidatorProvider.forEach(e -> preValidatorMap.put(e.supportType(), e));
		return new PreValidatorHolder(preValidatorMap);

	}

	private PreValidator buildDefaultValidator(String supportType) {
		return new PreValidator() {
			@Override
			public String supportType() {
				return supportType;
			}

			@Override
			public void validate(Map<String, String> parameters) {
				throw new RuntimeException(supportType + "前置验证器,未自定义");
			}
		};
	}

	/**
	 * 授权类型建造者，默认处理了 OAuth2 规范的 5 种授权类型，用户可自定义添加其他授权类型，如手机号登录
	 * @param authenticationManager 认证管理器
	 * @return TokenGrantBuilder
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenGrantBuilder tokenGrantBuilder(AuthenticationManager authenticationManager,
			PreValidatorHolder preValidatorHolder) {

		// 添加验证码授权模式授权者
		TokenGranterProvider captchaTokenGranter = endpoints -> new CaptchaTokenGranter(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager,
				Optional.ofNullable(preValidatorHolder.getByType(CaptchaTokenGranter.GRANT_TYPE))
						.orElseGet(() -> buildDefaultValidator(CaptchaTokenGranter.GRANT_TYPE)));
		// 添加手机短信验证码授权模式的授权者
		// 添加验证码授权模式授权者
		TokenGranterProvider smsCodeTokenGranter = endpoints -> new SmsCodeTokenGranter(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), authenticationManager,
				Optional.ofNullable(preValidatorHolder.getByType(SmsCodeTokenGranter.GRANT_TYPE))
						.orElseGet(() -> buildDefaultValidator(SmsCodeTokenGranter.GRANT_TYPE)));
		List<TokenGranterProvider> tokenGranterProviderList = new ArrayList<>();
		tokenGranterProviderList.add(captchaTokenGranter);
		tokenGranterProviderList.add(smsCodeTokenGranter);
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
	public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider(UserDetailsService userDetailsService) {
		return new SmsCodeAuthenticationProvider(userDetailsService);
	}

	/**
	 * 授权信息处理器
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthorizationInfoHandle authorizationInfoHandle() {
		return new AuthorizationInfoHandle().grantType("password", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				return userDetailsService.loadUserByUsername(name);
			}
		}).grantType("sms_code", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				ExtendUserDetailsService extendUserDetailsService = (ExtendUserDetailsService) userDetailsService;
				return extendUserDetailsService.loginByMobile(name);
			}
		});
	}

	/**
	 * token service 构建者
	 * @param userDetailsService
	 * @param authorizationInfoHandle
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenServicesBuilder tokenServicesBuilder(UserDetailsService userDetailsService,
			AuthorizationInfoHandle authorizationInfoHandle, TokenStore tokenStore) {
		return new TokenServicesBuilder(userDetailsService, authorizationInfoHandle, tokenStore);
	}

}
