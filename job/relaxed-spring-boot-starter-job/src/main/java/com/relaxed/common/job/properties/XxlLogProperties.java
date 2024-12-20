package com.relaxed.common.job.properties;

import ch.qos.logback.classic.Level;
import lombok.Data;

/**
 * @author Yakir
 * @Topic XxlLogProperties
 * @Description
 * @date 2024/12/20 17:32
 * @Version 1.0
 */
@Data
public class XxlLogProperties {

	private boolean enabled = true;

	private String name = "logXXl";

	private String logPrefix = "XXL-";

	private String[] includePackages;

	private String[] excludePackages;

	private Level minLevel = Level.INFO;

}
