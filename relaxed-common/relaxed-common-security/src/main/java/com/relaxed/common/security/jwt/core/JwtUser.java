package com.relaxed.common.security.jwt.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Yakir
 * @Topic JwtUser
 * @Description
 * @date 2021/8/11 20:51
 * @Version 1.0
 */
@Data
public class JwtUser  implements UserDetails {
    /**
     * 用户名
     */
    private  String username;

    /**
     * token 信息
     */
    private String token;

    /**
     * 权限信息列表
     */
    private final Collection<? extends GrantedAuthority> authorities;
    /**
     * 登录时间
     */
    private Long loginTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
