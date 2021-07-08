package com.relaxed.common.swagger.property;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import springfox.documentation.spi.DocumentationType;

/**
 * @author Yakir
 * @Topic DocumentationTypeEnum
 * @Description
 * @date 2021/7/8 12:59
 * @Version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum DocumentationTypeEnum {

	/**
	 * swagger 1.2
	 */
	SWAGGER_12(DocumentationType.SWAGGER_12),
	/**
	 * swagger2.0
	 */
	SWAGGER_2(DocumentationType.SWAGGER_2),

	/**
	 * swagger 3.0 openApi
	 */
	OAS_30(DocumentationType.OAS_30);

	private final DocumentationType type;

}
