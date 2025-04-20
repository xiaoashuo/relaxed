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
 * OAuth2授权服务器自动配置类 用于配置OAuth2授权服务器的核心组件 主要功能: 1. 配置令牌存储 2. 配置访问令牌转换器 3. 配置异常处理 4. 配置认证提供者
 * 5. 配置授权类型 6. 配置客户端信息
 *
 * @author Yakir
 * @since 1.0.0
 */
@Import({ CustomWebSecurityConfigurer.class, CustomAuthorizationServerConfigurer.class })
public class AuthorizationAutoConfiguration {

	/**
	 * 配置令牌存储 默认使用内存存储,可自定义实现
	 * @return TokenStore 令牌存储实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	/**
	 * 配置访问令牌转换器 用于将token字符串转换成OAuth2AccessToken对象
	 * @return AccessTokenConverter 访问令牌转换器
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessTokenConverter accessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}

	/**
	 * 配置自定义的认证异常转换器 用于处理OAuth2认证过程中的异常
	 * @return WebResponseExceptionTranslator 异常转换器
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebResponseExceptionTranslator customWebResponseExceptionTranslator() {
		return new CustomWebResponseExceptionTranslator();
	}

	/**
	 * 配置密码编码器 用于处理密码的加密和验证
	 * @return PasswordEncoder 密码编码器
	 */
	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return PasswordUtils.ENCODER;
	}

	/**
	 * 配置认证入口点 用于处理未认证的请求
	 * @return AuthenticationEntryPoint 认证入口点
	 */
	@Bean
	@ConditionalOnMissingBean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	/**
	 * 配置访问拒绝处理器 用于处理权限不足的请求
	 * @return AccessDeniedHandler 访问拒绝处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	/**
	 * 配置OAuth2客户端配置器 默认使用JDBC从数据库获取客户端信息
	 * @param dataSource 数据源
	 * @return OAuth2ClientConfigurer 客户端配置器
	 */
	@Bean
	@ConditionalOnMissingBean
	public OAuth2ClientConfigurer oAuth2ClientConfigurer(DataSource dataSource) {
		return new JdbcOauth2ClientConfigurer(dataSource);
	}

	/**
	 * 配置预检查持有器 用于管理各种预检查验证器
	 * @param preValidatorProvider 预检查验证器提供者
	 * @return PreValidatorHolder 预检查持有器
	 */
	@Bean
	public PreValidatorHolder preValidatorHolder(ObjectProvider<PreValidator> preValidatorProvider) {
		Map<String, PreValidator> preValidatorMap = new HashMap<>(8);
		preValidatorProvider.forEach(e -> preValidatorMap.put(e.supportType(), e));
		return new PreValidatorHolder(preValidatorMap);
	}

	/**
	 * 构建默认的预检查验证器 当未找到对应的验证器时使用
	 * @param supportType 支持的验证类型
	 * @return PreValidator 预检查验证器
	 */
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
	 * 配置授权类型构建器 默认处理OAuth2规范的5种授权类型 支持自定义添加其他授权类型
	 * @param authenticationManager 认证管理器
	 * @param preValidatorHolder 预检查持有器
	 * @return TokenGrantBuilder 授权类型构建器
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
	 * 配置用户名密码认证提供者 用于处理用户名密码认证
	 * @param passwordEncoder 密码编码器
	 * @param userDetailsService 用户详情服务
	 * @return DaoAuthenticationProvider 认证提供者
	 */
	@Bean
	@ConditionalOnMissingBean
	public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	/**
	 * 配置手机验证码认证提供者 用于处理手机验证码认证
	 * @param userDetailsService 用户详情服务
	 * @return SmsCodeAuthenticationProvider 手机验证码认证提供者
	 */
	@Bean
	public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider(UserDetailsService userDetailsService) {
		return new SmsCodeAuthenticationProvider(userDetailsService);
	}

	/**
	 * 配置授权信息处理器 用于处理不同授权类型的用户信息获取
	 * @return AuthorizationInfoHandle 授权信息处理器
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
	 * 配置令牌服务构建器 用于构建令牌服务
	 * @param userDetailsService 用户详情服务
	 * @param authorizationInfoHandle 授权信息处理器
	 * @param tokenStore 令牌存储
	 * @return TokenServicesBuilder 令牌服务构建器
	 */
	@Bean
	@ConditionalOnMissingBean
	public TokenServicesBuilder tokenServicesBuilder(UserDetailsService userDetailsService,
			AuthorizationInfoHandle authorizationInfoHandle, TokenStore tokenStore) {
		return new TokenServicesBuilder(userDetailsService, authorizationInfoHandle, tokenStore);
	}

}
