package com.relaxed.autoconfigure.web.jackson;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.core.jackson.JavaTimeModule;
import com.relaxed.common.core.jackson.NullSerializerModifier;
import com.relaxed.common.desensitize.json.DesensitizeStrategy;
import com.relaxed.common.desensitize.json.JsonDesensitizeModule;
import com.relaxed.common.desensitize.json.JsonDesensitizeSerializerModifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:14
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {

	/**
	 * 自定义objectMapper
	 * @return ObjectMapper
	 */
	@Bean
	@ConditionalOnClass(ObjectMapper.class)
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		// org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.JacksonObjectMapperConfiguration
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();

		// 序列化时忽略未知属性
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// NULL值修改
		objectMapper.setSerializerFactory(
				objectMapper.getSerializerFactory().withSerializerModifier(new NullSerializerModifier()));
		// 时间解析器
		objectMapper.registerModule(new JavaTimeModule());
		// 有特殊需要转义字符, 不报错
		objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());

		return objectMapper;
	}

	/**
	 * 注册 Jackson 的脱敏模块
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean({ JsonDesensitizeModule.class, DesensitizeStrategy.class })
	public JsonDesensitizeModule jsonDesensitizeModule() {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier();
		return new JsonDesensitizeModule(desensitizeModifier);
	}

	/**
	 * 注册 Jackson 的脱敏模块
	 * @return Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(DesensitizeStrategy.class)
	public JsonDesensitizeModule jsonDesensitizeModule(DesensitizeStrategy desensitizeStrategy) {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier(
				desensitizeStrategy);
		return new JsonDesensitizeModule(desensitizeModifier);
	}

}
