package com.relaxed.oauth2.resource;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author Yakir
 * @Topic RemoteTokenServiceProvider
 * @Description
 * @date 2022/7/24 17:50
 * @Version 1.0
 */
@FunctionalInterface
public interface RemoteTokenServiceProvider {

	ResourceServerTokenServices provide(ResourceServerProperties resource);

}
