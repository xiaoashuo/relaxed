package com.relaxed.common.swagger.annotation;

import com.relaxed.common.swagger.SwaggerProviderAutoConfiguration;
import com.relaxed.common.swagger.property.SwaggerProviderProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic EnableSwagger2Provider
 * @Description
 * @date 2021/7/8 12:59
 * @Version 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ SwaggerProviderAutoConfiguration.class })
public @interface EnableSwagger2Provider {

}
