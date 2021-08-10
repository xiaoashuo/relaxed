package com.relaxed.common.swagger.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yakir
 * @Topic SecuritySchemaEnum
 * @Description
 * @date 2021/8/10 14:19
 * @Version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum SecuritySchemaEnum {

	/**
	 * oauth2
	 */
	OATH2("OAuth2"),
	/**
	 * api key can be "header", "query" or "cookie"
	 */
	API_KEY("API KEY"),
	/**
	 * HTTP
	 */
	HTTP("http"),
	/**
	 * OPEN ID
	 */
	OPEN_ID_CONNECT("OPEN_ID_CONNECT")

	;

	private final String desc;

}
