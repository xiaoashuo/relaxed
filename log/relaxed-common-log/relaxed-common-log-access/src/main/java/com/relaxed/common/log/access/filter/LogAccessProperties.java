package com.relaxed.common.log.access.filter;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yakir
 * @Topic LogAccessProperties
 * @Description
 * @date 2024/12/23 17:58
 * @Version 1.0
 */
@Data
public class LogAccessProperties {

	public static final String PREFIX = "relaxed.log.access";

	/**
	 * 是否启用
	 */
	private boolean enabled;

	/**
	 * URL规则
	 */
	private List<LogAccessRule> urlRules = new ArrayList<>();

}
