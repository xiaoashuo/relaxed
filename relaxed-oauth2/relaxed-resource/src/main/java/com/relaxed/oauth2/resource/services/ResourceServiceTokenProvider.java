package com.relaxed.oauth2.resource.services;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author Yakir
 * @Topic ResourceServiceTokenProvider
 * @Description
 * @date 2022/7/29 14:51
 * @Version 1.0
 */
public interface ResourceServiceTokenProvider {

	/**
	 * 提供资源服务
	 * @author yakir
	 * @date 2022/7/29 14:52
	 * @param resourceServerProperties
	 * @return org.springframework.security.oauth2.provider.token.ResourceServerTokenServices
	 */
	ResourceServerTokenServices provide(ResourceServerProperties resourceServerProperties);

}
