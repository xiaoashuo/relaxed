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

	private LogAccessProperties access = new LogAccessProperties();

	private Operation operation = new Operation();

	private Biz biz = new Biz();

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
