package com.relaxed.extend.validate.code.generator;

import com.relaxed.extend.validate.code.domain.ValidateCode;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic ValidateCodeGenerator
 * @Description 验证码生成器
 * @date 2022/6/12 15:56
 * @Version 1.0
 */
public interface ValidateCodeGenerator {

	/**
	 * 生成验证码
	 * @author yakir
	 * @date 2022/6/12 16:00
	 * @param request
	 * @return com.relaxed.common.validate.code.domain.ValidateCode
	 */
	ValidateCode createValidateCode(HttpServletRequest request);

}
