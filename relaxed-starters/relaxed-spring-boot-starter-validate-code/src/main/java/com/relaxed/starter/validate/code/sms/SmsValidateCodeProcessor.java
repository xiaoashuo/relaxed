package com.relaxed.starter.validate.code.sms;

import cn.hutool.core.util.StrUtil;

import com.relaxed.extend.validate.code.ValidateCodeException;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;
import com.relaxed.extend.validate.code.processor.AbstractValidateCodeProcessor;
import com.relaxed.extend.validate.code.repository.ValidateCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yakir
 * @Topic SmsValidateCodeProcessor
 * @Description
 * @date 2022/6/12 16:53
 * @Version 1.0
 */
@Slf4j
public class SmsValidateCodeProcessor extends AbstractValidateCodeProcessor<SmsValidateCode> {

	public SmsValidateCodeProcessor(SmsProperties smsProperties, ValidateCodeRepository validateCodeRepository) {
		super(new SmsValidateCodeGenerator(smsProperties), validateCodeRepository, smsProperties);
	}

	@Override
	protected ValidateCodeType getValidateCodeType() {
		return ValidateCodeType.SMS;
	}

	@Override
	protected void send(HttpServletRequest request, HttpServletResponse response, SmsValidateCode validateCode)
			throws ValidateCodeException {
		// 手机号码
		String mobile = request.getParameter("mobile");
		String type = request.getParameter("type");
		if (StrUtil.isBlank(mobile) || StrUtil.isBlank(type)) {
			// 获取验证码参数没提供
			throw new ValidateCodeException("SMS_VALIDATE_CODE_PARAM_ERROR");
		}
		long minute = validateCode.minute();
		log.info("mock发送了短信。。。");

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// 写出发送短信成功
		// response.getOutputStream().write(CommonResponse.successInstance().toJson().getBytes());
	}

	@Override
	protected boolean validate(String code, SmsValidateCode validateCode) {
		return StrUtil.equalsIgnoreCase(code, validateCode.getCode());
	}

}
