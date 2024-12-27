package com.relaxed.common.redis.lock.scheduled;

import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic LockDefinitionHolder
 * @Description
 * @date 2022/10/12 16:31
 * @Version 1.0
 */
@Setter
@Getter
public class LockDefinitionHolder {

	/**
	 * 业务唯一 key
	 */
	private String lockKey;

	/**
	 * 加锁时间
	 */
	private Long lockTime;

	/**
	 * 时间单位 默认 TimeUnit.SECONDS
	 */
	private TimeUnit timeUnit;

	/**
	 * 上次更新时间（ms）
	 */
	private Long lastModifyTime;

	/**
	 * 保存当前线程
	 */
	private Thread currentTread;

	/**
	 * 总共尝试次数
	 */
	private int tryCount;

	/**
	 * 当前尝试次数
	 */
	private int currentCount;

	/**
	 * 更新的时间周期（毫秒）,公式 = 加锁时间（转成毫秒） / 3
	 */
	private Long modifyPeriod;

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
