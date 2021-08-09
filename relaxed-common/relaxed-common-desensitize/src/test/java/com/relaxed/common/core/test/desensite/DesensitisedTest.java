package com.relaxed.common.core.test.desensite;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.relaxed.common.desensitize.DesensitizationHandlerHolder;
import com.relaxed.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.relaxed.common.desensitize.enums.SlideDesensitizationTypeEnum;
import com.relaxed.common.desensitize.handler.RegexDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SimpleDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SixAsteriskDesensitizationHandler;
import com.relaxed.common.desensitize.handler.SlideDesensitizationHandler;
import com.relaxed.common.desensitize.json.JsonSerializerModifier;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Slf4j
class DesensitisedTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testSimple() {
		// 获取简单脱敏处理器
		SimpleDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
				.getSimpleHandler(SixAsteriskDesensitizationHandler.class);
		String origin = "你好吗？"; // 原始字符串
		String target = desensitizationHandler.handle(origin); // 替换处理
		Assert.isTrue("******".equals(target));
	}

	@Test
	void testRegex() {
		// 获取正则脱敏处理器
		RegexDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
				.getRegexDesensitizationHandler();
		String origin = "12123124213@qq.com"; // 原始字符串
		String regex = "(^.)[^@]*(@.*$)"; // 正则表达式
		String replacement = "$1****$2"; // 占位替换表达式
		String target1 = desensitizationHandler.handle(origin, regex, replacement); // 替换处理
		Assert.isTrue("1****@qq.com".equals(target1));

		// 内置的正则脱敏类型
		String target2 = desensitizationHandler.handle(origin, RegexDesensitizationTypeEnum.EMAIL);
		Assert.isTrue("1****@qq.com".equals(target2));
	}

	@Test
	void testSlide() {
		// 获取滑动脱敏处理器
		SlideDesensitizationHandler desensitizationHandler = DesensitizationHandlerHolder
				.getSlideDesensitizationHandler();
		String origin = "15805516789"; // 原始字符串
		String target1 = desensitizationHandler.handle(origin, 3, 2); // 替换处理
		Assert.isTrue("158******89".equals(target1));

		String target2 = desensitizationHandler.handle(origin, SlideDesensitizationTypeEnum.PHONE_NUMBER); // 替换处理
		Assert.isTrue("158******89".equals(target2));
	}

	@Test
	void testJackson() throws JsonProcessingException {
		// 指定DesensitizeHandler 若ignore方法为true 则忽略脱敏 false 则启用脱敏
		JsonSerializerModifier modifier = new JsonSerializerModifier((fieldName) -> {
			log.info("当前字段名称{}", fieldName);
			return false;
		});
		// 不指定 实现类 默认使用脱敏规则
		// JsonSerializerModifier modifier = new JsonSerializerModifier();

		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(modifier));
		DesensitizationUser user = new DesensitizationUser().setEmail("chengbohua@foxmail.com").setUsername("xiaoming")
				.setPassword("admina123456").setPhoneNumber("15800000000").setTestField("这是测试属性")
				.setCustomDesensitize("test");
		String value = objectMapper.writeValueAsString(user);

		Assert.isTrue(
				"{\"username\":\"xiaoming\",\"password\":\"adm****56\",\"email\":\"c****@foxmail.com\",\"phoneNumber\":\"158******00\",\"testField\":\"TEST-这是测试属性\",\"customDesensitize\":\"test\"}"
						.equals(value));

		log.info("脱敏后的数据：{}", value);
	}

}
