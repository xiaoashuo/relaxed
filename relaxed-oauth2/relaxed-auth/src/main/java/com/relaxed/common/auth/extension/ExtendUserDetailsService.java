package com.relaxed.common.auth.extension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Yakir
 * @Topic ExtendUserDetailService
 * @Description
 * @date 2022/7/23 10:37
 * @Version 1.0
 */
public interface ExtendUserDetailsService extends UserDetailsService {

	/**
	 * 加载用户信息 根据手机号
	 * @author yakir
	 * @date 2022/7/23 10:38
	 * @param mobile
	 * @return org.springframework.security.core.userdetails.UserDetails
	 */
	UserDetails loginByMobile(String mobile);

}
