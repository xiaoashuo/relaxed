package com.relaxed.oauth2.auth.annotation;

import com.relaxed.oauth2.auth.configuration.AuthorizationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * OAuth2授权服务器启用注解 用于启用OAuth2授权服务器功能 主要功能: 1. 启用Web安全配置 2. 启用授权服务器 3. 导入自动配置类 4. 支持继承配置
 *
 * @author Yakir
 * @since 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableWebSecurity
@EnableAuthorizationServer
@Import({ AuthorizationAutoConfiguration.class })
public @interface EnableOauth2AuthorizationServer {

}
