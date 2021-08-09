package com.relaxed.common.core.test.desensite;

import com.relaxed.common.core.test.desensite.custom.CustomerDesensitize;
import com.relaxed.common.desensitize.json.annotation.JsonRegexDesensitize;
import com.relaxed.common.desensitize.json.annotation.JsonSimpleDesensitize;
import com.relaxed.common.desensitize.json.annotation.JsonSlideDesensitize;
import com.relaxed.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.relaxed.common.desensitize.enums.SlideDesensitizationTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public class DesensitizationUser {

	/**
	 * 用户名，不脱敏
	 */
	private String username;

	/**
	 * 密码脱敏
	 */
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD)
	private String password;

	/**
	 * 邮件
	 */
	@JsonRegexDesensitize(type = RegexDesensitizationTypeEnum.EMAIL)
	private String email;

	/**
	 * 手机号
	 */
	@JsonSlideDesensitize(type = SlideDesensitizationTypeEnum.PHONE_NUMBER)
	private String phoneNumber;

	/**
	 * 测试自定义脱敏
	 */
	@JsonSimpleDesensitize(handler = TestDesensitizationHandler.class)
	private String testField;

	@CustomerDesensitize(type = "自定义注解")
	private String customDesensitize;

}
