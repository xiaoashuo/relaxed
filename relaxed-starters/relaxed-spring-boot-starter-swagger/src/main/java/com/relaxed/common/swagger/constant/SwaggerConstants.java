package com.relaxed.common.swagger.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yakir
 * @Topic SwaggerConstants
 * @Description
 * @date 2021/7/8 13:14
 * @Version 1.0
 */
public interface SwaggerConstants {

	/**
	 * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
	 */
	List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

	/**
	 * 默认扫描路径
	 */
	String DEFAULT_BASE_PATH = "/**";

}
