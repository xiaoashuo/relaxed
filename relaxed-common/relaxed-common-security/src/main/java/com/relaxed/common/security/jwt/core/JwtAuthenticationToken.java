package com.relaxed.common.security.jwt.core;

import lombok.Data;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Yakir
 * @Topic JwtAuthenticationToken
 * @Description
 * @date 2021/8/11 20:52
 * @Version 1.0
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 主体信息
     */
    private UserDetails principal;
    /**
     * 凭证
     */
    private String credentials;

    /**
     * token 信息
     */
    private String token;

    public JwtAuthenticationToken(UserDetails principal,String token,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal=principal;
        this.token=token;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        super.setAuthenticated(true);
    }

    public String getToken() {
        return token;
    }
}
