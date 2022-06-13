package com.relaxed.starter.validate.code.sms;

import com.relaxed.extend.validate.code.domain.ValidateCode;

/**
 * @author Yakir
 * @Topic SmsValidateCode
 * @Description
 * @date 2022/6/12 16:53
 * @Version 1.0
 */
public class SmsValidateCode extends ValidateCode {

	public SmsValidateCode(String code, long expireInSeconds) {
		super(code, expireInSeconds);
	}

}
