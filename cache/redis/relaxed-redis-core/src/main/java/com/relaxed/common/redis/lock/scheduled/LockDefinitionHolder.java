package com.relaxed.common.redis.lock.scheduled;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 锁定义持有者类。 用于存储分布式锁的相关信息，包括锁的键名、时间、线程等，支持锁续期功能。
 *
 * @author Yakir
 * @since 1.0
 */
@Setter
@Getter
public class LockDefinitionHolder {

	/**
	 * 锁的键名
	 */
	private String lockKey;

	/**
	 * 锁的过期时间
	 */
	private Long lockTime;

	/**
	 * 时间单位，默认秒
	 */
	private TimeUnit timeUnit;

	/**
	 * 上次更新时间（毫秒）
	 */
	private Long lastModifyTime;

	/**
	 * 持有锁的线程
	 */
	private Thread currentTread;

	/**
	 * 最大尝试续期次数
	 */
	private int tryCount;

	/**
	 * 当前已尝试续期次数
	 */
	private int currentCount;

	/**
	 * 续期时间周期（毫秒），计算公式：锁过期时间（转成毫秒）/ 3
	 */
	private Long modifyPeriod;

	/**
	 * 构造函数
	 * @param lockKey 锁的键名
	 * @param lockTime 锁的过期时间
	 * @param lastModifyTime 上次更新时间
	 * @param currentTread 持有锁的线程
	 * @param tryCount 最大尝试续期次数
	 * @param timeUnit 时间单位
	 */
	public LockDefinitionHolder(String lockKey, Long lockTime, Long lastModifyTime, Thread currentTread, int tryCount,
			TimeUnit timeUnit) {
		this.lockKey = lockKey;
		this.lockTime = lockTime;
		this.lastModifyTime = lastModifyTime;
		this.currentTread = currentTread;
		this.tryCount = tryCount;
		this.timeUnit = timeUnit;
		this.modifyPeriod = timeUnit.toMillis(lockTime) / 3;
	}

}
