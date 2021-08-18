package com.relaxed.common.security.jwt.provider;

import com.relaxed.common.security.jwt.filter.JwtAuthenticationToken;
import com.relaxed.common.security.jwt.core.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author Yakir
 * @Topic JwtAuthenticationProvider
 * @Description
 * @date 2021/8/11 21:12
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailService;

	private final JwtTokenService tokenService;

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	private UserCache userCache = new NullUserCache();

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
				() -> this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only JwtAuthenticationToken is supported"));
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String token = jwtAuthenticationToken.getToken();

		String username;
		try {
			username = determineUsername(token);
		}
		catch (Exception e) {
			throw new BadCredentialsException("Bad credentials");
		}
		if (!StringUtils.hasText(username)) {
			throw new BadCredentialsException("Bad credentials");
		}
		boolean cacheWasUsed = true;
		UserDetails user = this.userCache.getUserFromCache(username);
		if (user == null) {
			cacheWasUsed = false;
			try {
				user = retrieveUser(username);
				// 进行token认正
				tokenService.verify(token, user);
			}
			catch (UsernameNotFoundException ex) {
				log.debug("Failed to find user '" + username + "'");
				throw new BadCredentialsException("Bad credentials");
			}
			Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
		}

		if (!cacheWasUsed) {
			this.userCache.putUserInCache(user);
		}

		return createSuccessAuthentication(token, user);

	}

	protected Authentication createSuccessAuthentication(String token, UserDetails user) {
		// Ensure we return the original credentials the user supplied,
		// so subsequent attempts are successful even with encoded passwords.
		// Also ensure we return the original getDetails(), so that future
		// authentication events after cache expiry contain the details
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(user, token,
				this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
		log.debug("Authenticated user");
		return jwtAuthenticationToken;
	}

	UserDetails retrieveUser(String username) throws AuthenticationException {
		return userDetailService.loadUserByUsername(username);

	}

	private String determineUsername(String token) {
		return (token == null) ? "NONE_PROVIDED" : tokenService.getSubject(token);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(JwtAuthenticationToken.class);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messages = new MessageSourceAccessor(messageSource);
	}

	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		this.authoritiesMapper = authoritiesMapper;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

}
