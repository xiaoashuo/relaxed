package com.relaxed.oauth2.auth.handler;

import com.relaxed.oauth2.auth.functions.RetriveUserFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

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

	public AuthorizationInfoHandle client(String clientId, UserDetailsService userDetailsService) {
		this.CLIENT_USER_HOLDER.put(clientId, userDetailsService);
		return this;
	}

	public AuthorizationInfoHandle client(Map<String, UserDetailsService> clientMap) {
		this.CLIENT_USER_HOLDER.putAll(clientMap);
		return this;
	}

	public AuthorizationInfoHandle grantType(String grantType, RetriveUserFunction retriveUserFunction) {
		this.GRANT_TYPE_HANDLE_HOLDER.put(grantType, retriveUserFunction);
		return this;
	}

	public AuthorizationInfoHandle grantType(Map<String, RetriveUserFunction> grantTypeMap) {
		this.GRANT_TYPE_HANDLE_HOLDER.putAll(grantTypeMap);
		return this;
	}

	public RetriveUserFunction obtainFunction(String grantType) {
		return GRANT_TYPE_HANDLE_HOLDER.get(grantType);
	}

	public UserDetailsService obtainClient(String clientId) {
		return CLIENT_USER_HOLDER.get(clientId);
	}

}
