package com.relaxed.oauth2.auth.extension.captcha;

import java.util.Map;

/**
 * @author Yakir
 * @Topic CaptchaValidator
 * @Description
 * @date 2022/7/23 10:44
 * @Version 1.0
 */
public interface CaptchaValidator {

	/**
	 * 验证验证码正确性
	 * @author yakir
	 * @date 2022/7/23 10:51
	 * @param parameters 请求参数
	 */
	void validate(Map<String, String> parameters);

}
