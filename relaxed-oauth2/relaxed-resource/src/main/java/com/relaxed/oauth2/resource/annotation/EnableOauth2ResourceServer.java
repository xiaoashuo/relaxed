package com.relaxed.oauth2.resource.annotation;

import com.relaxed.oauth2.resource.configuration.ResourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

/**
 * 启用OAuth2资源服务器注解 用于启用OAuth2资源服务器的自动配置 包括资源服务器配置、方法级安全配置等
 *
 * @author Yakir
 * @since 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ ResourceAutoConfiguration.class })
public @interface EnableOauth2ResourceServer {

}
