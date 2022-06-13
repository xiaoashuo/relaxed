package com.relaxed.extend.validate.code.handler;

import com.relaxed.extend.validate.code.ValidateCodeException;
import com.relaxed.extend.validate.code.domain.ValidateCode;
import com.relaxed.extend.validate.code.domain.ValidateCodeType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yakir
 * @Topic ValidateCodeHandler
 * @Description 验证码处理器
 * @date 2022/6/12 15:56
 * @Version 1.0
 */
public interface ValidateCodeHandler<C extends ValidateCode> {

	/**
	 * 是否匹配成功
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	boolean support(HttpServletRequest request, ValidateCodeType validateCodeType);

	/**
	 * 开始处理发送验证码前的逻辑
	 * @param request
	 * @param validateCodeType
	 * @param validateCode
	 * @throws ValidateCodeException
	 */
	void beforeSend(HttpServletRequest request, ValidateCodeType validateCodeType, C validateCode)
			throws ValidateCodeException;

}
