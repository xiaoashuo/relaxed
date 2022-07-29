package com.relaxed.oauth2.auth.functions;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Yakir
 * @Topic RetriveUserFunction
 * @Description
 * @date 2022/7/29 10:50
 * @Version 1.0
 */
@FunctionalInterface
public interface RetriveUserFunction {

	<T extends Authentication> UserDetails retrive(T authentication, UserDetailsService userDetailsService);

}
