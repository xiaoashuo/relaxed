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
 * Jackson配置类 配置Jackson序列化和反序列化的行为 包括时间格式、空值处理、脱敏等功能
 *
 * @author Yakir
 * @since 1.0
 */
@Configuration
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfig {

	/**
	 * 创建自定义ObjectMapper 配置Jackson的序列化和反序列化行为
	 * @param builder Jackson对象映射器构建器
	 * @return 配置好的ObjectMapper
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
		// 有特殊需要转义字符, 不报错
		objectMapper.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());

		return objectMapper;
	}

	/**
	 * 注册自定义的Jackson时间格式模块 用于覆盖默认的时间格式
	 * @return 自定义时间格式模块
	 */
	@Bean
	@ConditionalOnMissingBean(JavaTimeModule.class)
	public JavaTimeModule customJavaTimeModule() {
		return new JavaTimeModule();
	}

	/**
	 * 注册Jackson的脱敏模块 用于处理敏感数据的序列化
	 * @return 脱敏模块
	 */
	@Bean
	@ConditionalOnMissingBean({ JsonDesensitizeModule.class, DesensitizeStrategy.class })
	public JsonDesensitizeModule jsonDesensitizeModule() {
		JsonDesensitizeSerializerModifier desensitizeModifier = new JsonDesensitizeSerializerModifier();
		return new JsonDesensitizeModule(desensitizeModifier);
	}

	/**
	 * 注册带策略的Jackson脱敏模块 使用指定的脱敏策略处理敏感数据
	 * @param desensitizeStrategy 脱敏策略
	 * @return 带策略的脱敏模块
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
