package com.relaxed.common.auth.annotation;

import com.relaxed.common.auth.configuration.AuthorizationAutoConfiguration;
import com.relaxed.common.auth.configurer.CustomWebSecurityConfigurer;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic EnableOauth2AuthorizationServer
 * @Description
 * @date 2022/7/24 11:36
 * @Version 1.0
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
