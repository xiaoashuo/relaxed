package com.relaxed.common.auth.extension.mobile;

/**
 * @author Yakir
 * @Topic SmsCodeHandler
 * @Description
 * @date 2022/7/23 10:14
 * @Version 1.0
 */
public interface SmsCodeValidator {

	/**
	 * 验证手机验证码 不正确则抛出异常
	 * @author yakir
	 * @date 2022/7/23 10:16
	 * @param mobile
	 * @param code
	 * @throws Throwable
	 */
	void authenticate(String mobile, String code);

}
