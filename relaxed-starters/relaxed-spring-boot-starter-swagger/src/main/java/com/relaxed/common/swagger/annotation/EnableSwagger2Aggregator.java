package com.relaxed.common.swagger.annotation;

import com.relaxed.common.swagger.SwaggerAggregatorAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Yakir
 * @Topic EnableSwagger2Aggregator
 * @Description
 * @date 2021/7/8 12:59
 * @Version 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ SwaggerAggregatorAutoConfiguration.class })
public @interface EnableSwagger2Aggregator {

}
