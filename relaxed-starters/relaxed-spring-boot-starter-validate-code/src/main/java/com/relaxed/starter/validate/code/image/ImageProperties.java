package com.relaxed.starter.validate.code.image;

import com.relaxed.extend.validate.code.domain.CodeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yakir
 * @Topic ImageProperties
 * @Description
 * @date 2022/6/12 17:04
 * @Version 1.0
 */

@ConfigurationProperties(prefix = "relaxed.v-code.image")
@Data
public class ImageProperties extends CodeProperties {

	private int length = 6;

	private int height = 23;

	private int width = 67;

	private String generatorUrl = "/code/image";

}
