package com.relaxed.oauth2.resource.annotation;

import com.relaxed.oauth2.resource.configuration.ResourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

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
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({ ResourceAutoConfiguration.class })
public @interface EnableOauth2ResourceServer {

}
