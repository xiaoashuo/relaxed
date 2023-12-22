package com.relaxed.autoconfigure.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yakir
 * @Topic LogProperties
 * @Description
 * @date 2023/12/22 14:02
 * @Version 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = LogProperties.ROOT_PREFIX)
public class LogProperties {

	public static final String ROOT_PREFIX = "relaxed.log";

	private Access access;

	private Operation operation;

	private Biz biz;

	@Data
	public class Access {

		public static final String PREFIX = ROOT_PREFIX + ".access";

		private boolean enabled;

		/**
		 * 忽略的Url匹配规则，Ant风格
		 */
		private List<String> ignoreUrlPatterns = Arrays.asList("/actuator/**", "/webjars/**", "/favicon.ico",
				"/captcha/get");

	}

	/**
	 * 操作日志
	 */
	@Data
	public class Operation {

		public static final String PREFIX = ROOT_PREFIX + ".operation";

		private boolean enabled;

	}

	/**
	 * 业务日志
	 */
	@Data
	public class Biz {

		public static final String PREFIX = ROOT_PREFIX + ".biz";

		private boolean enabled;

	}

}
