package com.relaxed.starter.validate.code.slide;

import com.relaxed.extend.validate.code.domain.CodeProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Yakir
 * @Topic SlideProperties
 * @Description
 * @date 2022/6/12 20:12
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SlideImageProperties extends CodeProperties {

	private String generatorUrl = "/code/slide";

}