package com.relaxed.autoconfigure.log.properties;

import com.relaxed.common.log.access.filter.LogAccessProperties;
import com.sun.javafx.css.Rule;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 日志配置属性类。 用于统一管理日志相关的配置属性。 主要功能包括： 1. 访问日志配置 2. 操作日志配置 3. 业务日志配置
 *
 * @author Yakir
 * @since 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = LogProperties.ROOT_PREFIX)
public class LogProperties {

	/**
	 * 日志配置根前缀
	 */
	public static final String ROOT_PREFIX = "relaxed.log";

	/**
	 * 访问日志配置属性
	 */
	private LogAccessProperties access = new LogAccessProperties();

	/**
	 * 操作日志配置属性
	 */
	private Operation operation = new Operation();

	/**
	 * 业务日志配置属性
	 */
	private Biz biz = new Biz();

	/**
	 * 操作日志配置类。 用于配置操作日志相关的属性。
	 */
	@Data
	public class Operation {

		/**
		 * 操作日志配置前缀
		 */
		public static final String PREFIX = ROOT_PREFIX + ".operation";

		/**
		 * 是否启用操作日志
		 */
		private boolean enabled;

	}

	/**
	 * 业务日志配置类。 用于配置业务日志相关的属性。
	 */
	@Data
	public class Biz {

		/**
		 * 业务日志配置前缀
		 */
		public static final String PREFIX = ROOT_PREFIX + ".biz";

		/**
		 * 是否启用业务日志
		 */
		private boolean enabled;

	}

}
