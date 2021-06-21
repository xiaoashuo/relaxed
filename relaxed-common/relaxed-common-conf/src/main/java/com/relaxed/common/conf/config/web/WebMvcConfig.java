package com.relaxed.common.conf.config.web;

import com.relaxed.common.conf.config.web.relover.PageParamArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web mvc config
 *
 * @author Yakir
 * @since 2021/3/19
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PageParamArgumentResolver());
	}

}
