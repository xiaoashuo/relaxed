package com.relaxed.starter.validate.code.sms;

import com.relaxed.extend.validate.code.domain.CodeProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Yakir
 * @Topic SmsProperties
 * @Description
 * @date 2022/6/12 19:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SmsProperties extends CodeProperties {

	private int length = 6;

	private String generatorUrl = "/code/sms";

}