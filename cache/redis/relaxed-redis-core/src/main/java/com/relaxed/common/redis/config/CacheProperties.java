package com.relaxed.common.redis.config;

import cn.hutool.core.io.watch.Watcher;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.concurrent.TimeUnit;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/3/20 16:56
 */
@Data
@ConfigurationProperties(prefix = "relaxed.redis")
public class CacheProperties {

	/**
	 * 通用的key前缀
	 */
	private String keyPrefix = "";

	/**
	 * redis锁 后缀
	 */
	private String lockKeySuffix = "locked";

	/**
	 * 默认分隔符
	 */
	private String delimiter = ":";

	/**
	 * 空值标识
	 */
	private String nullValue = "N_V";

	/**
	 * 默认超时时间(s)
	 */
	private long expireTime = 86400L;

	/**
	 * 锁续期 默认为false
	 */
	private boolean lockRenewal = false;

	/**
	 * 监听者
	 */
	@NestedConfigurationProperty
	private Watcher watcher;

	@Data
	public static class Watcher {

		/**
		 * 锁续期次数 默认3次
		 */
		private Integer lockRenewalCount = 3;

		/**
		 * 锁的超时时间(ms)
		 */
		private long lockedTimeOut = 1000L;

		/**
		 * 锁单位
		 */
		private TimeUnit lockTimeUnit = TimeUnit.SECONDS;

	}

}
