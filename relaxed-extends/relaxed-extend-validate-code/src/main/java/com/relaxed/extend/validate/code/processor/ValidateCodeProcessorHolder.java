package com.relaxed.extend.validate.code.processor;

import cn.hutool.core.util.StrUtil;

import com.relaxed.extend.validate.code.ValidateCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Yakir
 * @Topic ValidateCodeProcessorHolder
 * @Description
 * @date 2022/6/12 17:02
 * @Version 1.0
 */
@RequiredArgsConstructor
public class ValidateCodeProcessorHolder {

	private final List<ValidateCodeProcessor> validateCodeProcessorList;

	/**
	 * 验证操作 判断是否需要验证 需要则执行效验 否则不做任何处理
	 * @param request
	 * @param response
	 * @return
	 */
	public void validateCode(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURI();
		// 循环调用验证码处理器进行验证
		for (ValidateCodeProcessor processor : validateCodeProcessorList) {
			if (!processor.isValidateRequest(url)) {
				continue;
			}
			processor.validate(request);
		}
	}

	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @return
	 */
	public void generatorCode(HttpServletRequest request, HttpServletResponse response) {
		// 获取验证码只能通过GET请求
		if (!StrUtil.equalsIgnoreCase(request.getMethod(), HttpMethod.GET.name())) {
			throw new ValidateCodeException("Request not support,Required Get Request.");
		}
		String url = request.getRequestURI();
		// 依然还是通过验证码处理器去做生成验证码的操作
		for (ValidateCodeProcessor processor : validateCodeProcessorList) {
			// 检查当前请求是要生成哪种类型的验证码
			if (!processor.isGenerateRequest(url)) {
				continue;
			}
			processor.create(request, response);
		}
	}

}
