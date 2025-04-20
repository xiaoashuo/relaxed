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
 * 授权信息处理器 用于管理客户端和授权类型的处理器映射 支持动态注册客户端和授权类型的处理逻辑 如果找不到对应的处理器，则使用默认方式执行
 *
 * @author Yakir
 * @since 1.0
 */
public class AuthorizationInfoHandle {

	/**
	 * 客户端与用户详情服务的映射 key: 客户端ID value: 对应的用户详情服务
	 */
	private Map<String, UserDetailsService> CLIENT_USER_HOLDER = new ConcurrentHashMap<>();

	/**
	 * 授权类型与用户检索函数的映射 key: 授权类型 value: 对应的用户检索函数
	 */
	private Map<String, RetriveUserFunction> GRANT_TYPE_HANDLE_HOLDER = new ConcurrentHashMap<>();

	/**
	 * 注册单个客户端及其对应的用户详情服务
	 * @param clientId 客户端ID
	 * @param userDetailsService 用户详情服务
	 * @return 当前处理器实例，支持链式调用
	 */
	public AuthorizationInfoHandle client(String clientId, UserDetailsService userDetailsService) {
		this.CLIENT_USER_HOLDER.put(clientId, userDetailsService);
		return this;
	}

	/**
	 * 批量注册客户端及其对应的用户详情服务
	 * @param clientMap 客户端ID与用户详情服务的映射
	 * @return 当前处理器实例，支持链式调用
	 */
	public AuthorizationInfoHandle client(Map<String, UserDetailsService> clientMap) {
		this.CLIENT_USER_HOLDER.putAll(clientMap);
		return this;
	}

	/**
	 * 注册单个授权类型及其对应的用户检索函数
	 * @param grantType 授权类型
	 * @param retriveUserFunction 用户检索函数
	 * @return 当前处理器实例，支持链式调用
	 */
	public AuthorizationInfoHandle grantType(String grantType, RetriveUserFunction retriveUserFunction) {
		this.GRANT_TYPE_HANDLE_HOLDER.put(grantType, retriveUserFunction);
		return this;
	}

	/**
	 * 批量注册授权类型及其对应的用户检索函数
	 * @param grantTypeMap 授权类型与用户检索函数的映射
	 * @return 当前处理器实例，支持链式调用
	 */
	public AuthorizationInfoHandle grantType(Map<String, RetriveUserFunction> grantTypeMap) {
		this.GRANT_TYPE_HANDLE_HOLDER.putAll(grantTypeMap);
		return this;
	}

	/**
	 * 根据授权类型获取对应的用户检索函数
	 * @param grantType 授权类型
	 * @return 用户检索函数，如果不存在则返回null
	 */
	public RetriveUserFunction obtainFunction(String grantType) {
		return GRANT_TYPE_HANDLE_HOLDER.get(grantType);
	}

	/**
	 * 根据客户端ID获取对应的用户详情服务
	 * @param clientId 客户端ID
	 * @return 用户详情服务，如果不存在则返回null
	 */
	public UserDetailsService obtainClient(String clientId) {
		return CLIENT_USER_HOLDER.get(clientId);
	}

}
