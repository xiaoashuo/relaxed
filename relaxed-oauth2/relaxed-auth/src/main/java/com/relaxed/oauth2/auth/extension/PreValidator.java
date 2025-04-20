package com.relaxed.oauth2.auth.extension;

import java.util.Map;

/**
 * 预验证器接口 用于在用户认证之前进行额外的验证操作，如验证码校验等 实现此接口的类可以定义自己的验证逻辑，并通过supportType方法指定支持的验证类型
 *
 * @author Yakir
 * @since 1.0
 */
public interface PreValidator {

	/**
	 * 获取支持的验证类型 用于标识该验证器可以处理的验证类型，如短信验证码、图片验证码等
	 * @return 验证类型标识符
	 */
	String supportType();

	/**
	 * 执行验证操作 根据传入的参数进行验证，如果验证失败则抛出相应的异常
	 * @param parameters 验证所需的参数，如验证码、手机号等
	 * @throws RuntimeException 当验证失败时抛出异常
	 */
	void validate(Map<String, String> parameters);

}
