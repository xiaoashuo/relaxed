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
public class Oauth2Constant {

	/**
	 * 默认忽略鉴权地址
	 */
	public static final List<String> DEFAULT_IGNORE_AUTH_URL = ListUtil.toList("/oauth/**");

}
