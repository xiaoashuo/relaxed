package com.relaxed.common.cache.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic MetaAnnotationInfo
 * @Description
 * @date 2021/7/24 15:07
 * @Version 1.0
 */
@Data
public class MetaAnnotationInfo {

	private String prefix;

	private String key;

	private String suffix;

	private String condition;

	private String keyGenerate;

}
