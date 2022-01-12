package com.relaxed.common.security.jwt.core;

import com.relaxed.common.security.jwt.exception.JwtVerifyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Yakir
 * @Topic JwtTokenService
 * @Description
 * @date 2021/8/11 21:23
 * @Version 1.0
 */
public interface JwtTokenService<T extends UserDetails> {

	/**
	 * 生成token
	 * @author yakir
	 * @date 2021/8/15 12:51
	 * @param userDetails
	 * @return java.lang.String
	 */
	String generateToken(T userDetails);

	/**
	 * 得到主体用户名
	 * @author yakir
	 * @date 2021/8/11 21:24
	 * @return java.lang.String
	 * @param token
	 */
	String getSubject(String token);

	/**
	 * 效验token 若错误直接抛出异常
	 * @author yakir
	 * @date 2021/8/15 10:24
	 * @param token
	 * @param user
	 * @throws JwtVerifyException
	 */
	void verify(String token, T user) throws JwtVerifyException;

	/**
	 * 是否应该刷新token
	 * @author yakir
	 * @date 2021/8/15 10:30
	 * @param token
	 * @return boolean
	 */
	boolean shouldTokenRefresh(String token);

}
