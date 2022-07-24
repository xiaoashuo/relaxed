package com.relaxed.common.auth.builder;

import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;

/**
 * @author Yakir
 * @Topic TokenGranterProvider
 * @Description
 * @date 2022/7/23 11:01
 * @Version 1.0
 */
@FunctionalInterface
public interface TokenGranterProvider {

	TokenGranter provide(final AuthorizationServerEndpointsConfigurer endpoints);

}
