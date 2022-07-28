package com.relaxed.oauth2.auth.extension.handler;

import com.relaxed.oauth2.auth.extension.functions.RetriveUserFunction;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * @author Yakir
 * @Topic ClientHandlerConfigurer
 * @Description
 * @date 2022/7/28 20:49
 * @Version 1.0
 */
public interface ClientHandlerConfigurer {

	/**
	 * 配置客户端 对应的处理器 等同于优先选择器
	 * @author yakir
	 * @date 2022/7/28 20:52
	 * @param clientMap
	 */
	void Client(Map<String, UserDetailsService> clientMap);

	/**
	 * 配置授权类型对应的处理器
	 * @param grantTypeMap
	 */
	void grantTyp(Map<String, RetriveUserFunction> grantTypeMap);

}
