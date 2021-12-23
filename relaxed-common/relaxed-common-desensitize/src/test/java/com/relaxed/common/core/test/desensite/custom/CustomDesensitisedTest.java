package com.relaxed.common.core.test.desensite.custom;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.desensitize.AnnotationHandlerHolder;
import com.relaxed.common.desensitize.DesensitizationHandlerHolder;
import com.relaxed.common.desensitize.json.JsonDesensitizeSerializerModifier;
import com.relaxed.common.core.test.desensite.DesensitizationUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Slf4j
class CustomDesensitisedTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void desensitizedExtend() throws JsonProcessingException {
		// 注册自定义脱敏类型处理器
		CustomDesensitisedHandler customDesensitisedHandler = new CustomDesensitisedHandler();
		DesensitizationHandlerHolder.addHandler(CustomDesensitisedHandler.class, customDesensitisedHandler);
		// 注册注解 处理器
		AnnotationHandlerHolder.addHandleFunction(CustomerDesensitize.class, (annotation, value) -> {
			CustomerDesensitize customerDesensitize = (CustomerDesensitize) annotation;
			String type = customerDesensitize.type();
			log.info("注解上的参数{}", type);
			CustomDesensitisedHandler handler = (CustomDesensitisedHandler) DesensitizationHandlerHolder
					.getHandler(CustomDesensitisedHandler.class);
			return handler.handle(value);
		});
		// 初始化序列号modifier
		JsonDesensitizeSerializerModifier modifier = new JsonDesensitizeSerializerModifier();
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));

		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com").setUsername("xiaoming")
				.setPassword("admina123456").setPhoneNumber("15800000000").setTestField("这是测试属性")
				.setCustomDesensitize("自定义属性");
		String value = objectMapper.writeValueAsString(user);
		log.info("脱敏后的数据：{}", value);
		Assert.isTrue(
				"{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158******00\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"customer rule自定义属性\"}"
						.equals(value));
	}

}
