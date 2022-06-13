package com.relaxed.extend.validate.code.processor;

import com.relaxed.extend.validate.code.ValidateCodeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yakir
 * @Topic ValidateCodeProcessor
 * @Description 验证码处理器
 * @date 2022/6/12 15:53
 * @Version 1.0
 */
public interface ValidateCodeProcessor {

	/**
	 * 是否为生成验证码请求
	 * @author yakir
	 * @date 2022/6/12 17:34
	 * @param targetUrl 当前url
	 * @return boolean true 是 false不是
	 */
	boolean isGenerateRequest(String targetUrl);

	/**
	 * 是否为需要效验请求
	 * @author yakir
	 * @date 2022/6/12 17:20
	 * @param targetUrl 当前url
	 * @return boolean true 需要验证 false不需要
	 */
	boolean isValidateRequest(String targetUrl);

	/**
	 * 创建验证码
	 * @author yakir
	 * @date 2022/6/12 15:54
	 * @param request
	 */
	void create(HttpServletRequest request, HttpServletResponse response) throws ValidateCodeException;

	/**
	 * 校验验证码
	 * @author yakir
	 * @date 2022/6/12 15:54
	 * @param request
	 * @throws ValidateCodeException
	 */
	void validate(HttpServletRequest request) throws ValidateCodeException;

}
