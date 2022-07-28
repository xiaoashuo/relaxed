package com.relaxed.oauth2.auth.extension.handler;

import com.relaxed.oauth2.auth.extension.functions.RetriveUserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yakir
 * @Topic UserDetailServiceHolder
 * @Description 客户端处理器映射,若找不到映射客户端 ，则以默认方式执行
 * @date 2022/7/28 18:15
 * @Version 1.0
 */

public class AuthorizationInfoHandle {

	/** 客户端 对应处理service */
	private Map<String, UserDetailsService> CLIENT_USER_HOLDER = new ConcurrentHashMap<>();

	/** 授权类型处理器注册 */
	private Map<String, RetriveUserFunction> GRANT_TYPE_HANDLE_HOLDER = new ConcurrentHashMap<>();

	@Autowired
	private List<ClientHandlerConfigurer> configurers = Collections.emptyList();

	@PostConstruct
	public void init() {
		for (ClientHandlerConfigurer configurer : configurers) {
			try {
				configurer.Client(CLIENT_USER_HOLDER);
				configurer.grantTyp(GRANT_TYPE_HANDLE_HOLDER);
			}
			catch (Exception e) {
				throw new IllegalStateException("Cannot configure enpdoints", e);
			}
		}
	}

	public RetriveUserFunction obtainFunction(String grantType) {
		return GRANT_TYPE_HANDLE_HOLDER.get(grantType);
	}

	public UserDetailsService obtainClient(String clientId) {
		return CLIENT_USER_HOLDER.get(clientId);
	}

}
