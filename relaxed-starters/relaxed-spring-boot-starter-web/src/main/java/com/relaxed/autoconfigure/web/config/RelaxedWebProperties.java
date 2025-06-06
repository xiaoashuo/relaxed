package com.relaxed.autoconfigure.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebProperties
 *
 * @author Yakir
 */
@ConfigurationProperties("relaxed.web")
@Data
public class RelaxedWebProperties {

	/**
	 * 生产环境错误屏蔽 默认为true
	 */
	private boolean maskError = true;

	/**
	 * 分页大小限制
	 */
	private int pageSizeLimit = 100;

}
