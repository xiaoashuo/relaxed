package com.relaxed.extend.validate.code.repository;

import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic ValidateCodeRepository
 * @Description
 * @date 2022/6/12 15:57
 * @Version 1.0
 */
public interface ValidateCodeRepository {

	/**
	 * 保存验证码
	 * @param request
	 * @param code
	 * @param validateCodeType
	 */
	void save(HttpServletRequest request, ValidateCode code, ValidateCodeType validateCodeType, String codeKeyValue);

	/**
	 * 获取验证码
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	ValidateCode get(HttpServletRequest request, ValidateCodeType validateCodeType, String codeKeyValue);

	/**
	 * 移除验证码
	 * @param request
	 * @param codeType
	 */
	void remove(HttpServletRequest request, ValidateCodeType codeType, String codeKeyValue);

}
