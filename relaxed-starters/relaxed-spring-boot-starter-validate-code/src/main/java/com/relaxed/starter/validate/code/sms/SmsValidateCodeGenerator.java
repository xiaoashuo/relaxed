package com.relaxed.starter.validate.code.sms;

import cn.hutool.core.util.RandomUtil;
import com.relaxed.extend.validate.code.generator.ValidateCodeGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic SmsValidateCodeGenerator
 * @Description
 * @date 2022/6/12 16:53
 * @Version 1.0
 */
public class SmsValidateCodeGenerator implements ValidateCodeGenerator {

	private final SmsProperties smsProperties;

	public SmsValidateCodeGenerator(SmsProperties smsProperties) {
		this.smsProperties = smsProperties;
	}

	@Override
	public SmsValidateCode createValidateCode(HttpServletRequest request) {
		String code = RandomUtil.randomNumbers(smsProperties.getLength());
		return new SmsValidateCode(code, smsProperties.getExpiredInSecond());
	}

}
