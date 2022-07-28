package com.relaxed.oauth2.common.constant;

import cn.hutool.core.collection.ListUtil;

import java.util.List;

/**
 * @author Yakir
 * @Topic Oauth2Constant
 * @Description
 * @date 2022/7/26 13:53
 * @Version 1.0
 */
public interface SecurityConstant {

	/**
	 * 默认忽略鉴权地址
	 */
	List<String> DEFAULT_IGNORE_AUTH_URL = ListUtil.toList("/oauth/**");

	String CLIENT_ID_KEY = "client_id";

	/**
	 * 认证请求头key
	 */
	String AUTHORIZATION_KEY = "Authorization";

	/**
	 * Basic认证前缀
	 */
	String BASIC_PREFIX = "Basic ";

	String GRANT_TYPE_KEY = "grant_type";

}
