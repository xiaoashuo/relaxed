package com.relaxed.extend.validate.code.domain;

import lombok.Data;

/**
 * @author Yakir
 * @Topic CodeProperties
 * @Description
 * @date 2022/6/12 17:04
 * @Version 1.0
 */

@Data
public class CodeProperties {

	private long expiredInSecond = 300;

	private String[] filterUrls;

	private String generatorUrl;

}
