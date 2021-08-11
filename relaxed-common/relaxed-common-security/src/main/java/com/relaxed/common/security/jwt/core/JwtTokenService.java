package com.relaxed.common.security.jwt.core;

/**
 * @author Yakir
 * @Topic JwtTokenService
 * @Description
 * @date 2021/8/11 21:23
 * @Version 1.0
 */
public interface JwtTokenService {
    /**
     * 得到主体用户名
     * @author yakir
     * @date 2021/8/11 21:24
     * @return java.lang.String
     * @param token
     */
    String getSubject(String token);
}
