package com.relaxed.oauth2.auth.handler;

import com.relaxed.oauth2.auth.extension.ExtendUserDetailsService;
import com.relaxed.oauth2.auth.functions.RetriveUserFunction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

/**
 * @author Yakir
 * @Topic CustomClientHandlerConfigurer
 * @Description
 * @date 2022/7/29 10:47
 * @Version 1.0
 */
public class CustomClientHandlerConfigurer implements ClientHandlerConfigurer {

	@Override
	public void Client(Map<String, UserDetailsService> clientMap) {

	}

	@Override
	public void grantType(Map<String, RetriveUserFunction> grantTypeMap) {
		grantTypeMap.put("password", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				return userDetailsService.loadUserByUsername(name);
			}
		});
		grantTypeMap.put("sms_code", new RetriveUserFunction() {
			@Override
			public <T extends Authentication> UserDetails retrive(T authentication,
					UserDetailsService userDetailsService) {
				String name = authentication.getName();
				ExtendUserDetailsService extendUserDetailsService = (ExtendUserDetailsService) userDetailsService;
				return extendUserDetailsService.loginByMobile(name);
			}
		});
	}

}
