package com.relaxed.starter.validate.code;

import com.relaxed.extend.validate.code.ValidateCodeFilter;
import com.relaxed.extend.validate.code.processor.ValidateCodeProcessor;
import com.relaxed.extend.validate.code.processor.ValidateCodeProcessorHolder;
import com.relaxed.extend.validate.code.repository.LocalValidateCodeRepository;
import com.relaxed.extend.validate.code.repository.ValidateCodeRepository;
import com.relaxed.starter.validate.code.image.ImageProperties;
import com.relaxed.starter.validate.code.image.ImageValidateCodeProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.List;

/**
 * @author Yakir
 * @Topic ValidateCodeAutoConfiguration
 * @Description
 * @date 2022/6/12 17:53
 * @Version 1.0
 */
@Slf4j
@Configuration
public class ValidateCodeAutoConfiguration {

	/**
	 * 验证码过滤器
	 * @author yakir
	 * @date 2022/6/12 17:56
	 * @param validateCodeProcessorHolder
	 * @return org.springframework.boot.web.servlet.FilterRegistrationBean<com.relaxed.common.validate.code.ValidateCodeFilter>
	 */
	// @Bean
	// public FilterRegistrationBean<ValidateCodeFilter>
	// validateCodeFilterRegistrationBean(ValidateCodeProcessorHolder
	// validateCodeProcessorHolder) {
	// log.debug("验证码 过滤已开启====");
	// ValidateCodeFilter validateCodeFilter = new
	// ValidateCodeFilter(validateCodeProcessorHolder);
	// FilterRegistrationBean<ValidateCodeFilter> registrationBean = new
	// FilterRegistrationBean<>(validateCodeFilter);
	// registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	// return registrationBean;
	// }
	/**
	 * 验证码处理器
	 * @author yakir
	 * @date 2022/6/12 17:54
	 * @param validateCodeProcessorList
	 * @return com.relaxed.common.validate.code.processor.ValidateCodeProcessorHolder
	 */
	@Bean
	public ValidateCodeProcessorHolder validateCodeProcessorHolder(
			List<ValidateCodeProcessor> validateCodeProcessorList) {
		return new ValidateCodeProcessorHolder(validateCodeProcessorList);
	}

	/**
	 * 本地验证码存储
	 * @author yakir
	 * @date 2022/6/12 17:54
	 * @return com.relaxed.common.validate.code.repository.ValidateCodeRepository
	 */
	@Bean
	public ValidateCodeRepository localValidateCodeRepository() {
		return new LocalValidateCodeRepository();
	}

	@Bean
	@ConditionalOnProperty(prefix = "relaxed.v-code.image", name = "enabled", havingValue = "true")
	public ImageProperties imageProperties() {
		return new ImageProperties();
	}

	@Bean
	@ConditionalOnProperty(prefix = "relaxed.v-code.image", name = "enabled", havingValue = "true")
	public ValidateCodeProcessor imageValidateCodeProcessor(ImageProperties imageProperties,
			ValidateCodeRepository validateCodeRepository) {
		return new ImageValidateCodeProcessor(imageProperties, validateCodeRepository);
	}

}
