package com.relaxed.common.security.jwt.core;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Yakir
 * @Topic JwtUserDetailService
 * @Description
 * @date 2021/8/11 21:09
 * @Version 1.0
 */
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return User.builder().username("Jack").password("$2a$10$2dK6H5pOf.0xdY18Kw6Xlu0BvgjCaaNgM1GkCUWlSRHwafmqa2WM.").roles("USER").build();

    }
}
