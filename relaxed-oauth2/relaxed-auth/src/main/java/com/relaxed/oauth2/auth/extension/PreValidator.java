package com.relaxed.oauth2.auth.extension;

import java.util.Map;

/**
 * @author Yakir
 * @Topic PreValidator
 * @Description
 * @date 2022/8/16 17:06
 * @Version 1.0
 */
public interface PreValidator {

	/**
	 * 支持验证类型
	 * @return
	 */
	String supportType();

	/**
	 * 验证验证码正确性
	 * @author yakir
	 * @date 2022/7/23 10:51
	 * @param parameters 请求参数
	 */
	void validate(Map<String, String> parameters);

}
