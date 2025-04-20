package com.relaxed.oauth2.common.constant;

import cn.hutool.core.collection.ListUtil;

import java.util.List;

/**
 * 安全相关常量接口 定义OAuth2认证过程中使用的常量 包括认证请求头、认证类型、Token类型等
 *
 * @author Yakir
 * @since 1.0
 */
public interface SecurityConstant {

	/**
	 * 默认忽略鉴权的URL列表 这些URL不需要进行认证即可访问
	 */
	List<String> DEFAULT_IGNORE_AUTH_URL = ListUtil.toList("/oauth/**");

	/**
	 * 客户端ID的请求参数名
	 */
	String CLIENT_ID_KEY = "client_id";

	/**
	 * 认证请求头名称 用于传递认证信息
	 */
	String AUTHORIZATION_KEY = "Authorization";

	/**
	 * Basic认证前缀 用于标识Basic认证方式
	 */
	String BASIC_PREFIX = "Basic ";

	/**
	 * 授权类型的请求参数名
	 */
	String GRANT_TYPE_KEY = "grant_type";

	/**
	 * 刷新Token的请求参数名
	 */
	String REFRESH_TOKEN_KEY = "refresh_token";

}