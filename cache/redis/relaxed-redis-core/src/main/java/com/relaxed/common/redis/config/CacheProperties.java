package com.relaxed.common.redis.config;

import cn.hutool.core.io.watch.Watcher;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.concurrent.TimeUnit;

/**
 * Redis缓存配置属性类。 用于配置Redis缓存的基本属性，包括键前缀、分隔符、过期时间等。
 *
 * @author Hccake
 * @since 1.0
 */
@Data
@ConfigurationProperties(prefix = "relaxed.redis")
public class CacheProperties {

	/**
	 * 通用的key前缀，用于区分不同应用的缓存
	 */
	private String keyPrefix = "";

	/**
	 * Redis锁的键后缀，用于分布式锁的键名生成
	 */
	private String lockKeySuffix = "locked";

	/**
	 * 键名分隔符，用于连接键名的不同部分
	 */
	private String delimiter = ":";

	/**
	 * 空值标识，用于缓存空值避免缓存穿透
	 */
	private String nullValue = "N_V";

	/**
	 * 默认缓存过期时间，单位：秒
	 */
	private long expireTime = 86400L;

	/**
	 * 是否启用锁续期功能
	 */
	private boolean lockRenewal = false;

	/**
	 * 锁续期监听配置
	 */
	@NestedConfigurationProperty
	private Watcher watcher;

	/**
	 * 锁续期监听器配置类
	 */
	@Data
	public static class Watcher {

		/**
		 * 锁续期的最大次数
		 */
		private Integer lockRenewalCount = 3;

		/**
		 * 锁的超时时间，单位：毫秒
		 */
		private long lockedTimeOut = 1000L;

		/**
		 * 锁超时时间的时间单位
		 */
		private TimeUnit lockTimeUnit = TimeUnit.SECONDS;

	}

}
