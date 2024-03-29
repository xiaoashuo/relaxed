package com.relaxed.common.redis.operation;

import com.relaxed.common.redis.config.CachePropertiesHolder;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Yakir
 * @Topic AbstractCacheOps
 * @Description
 * @date 2021/7/23 18:08
 * @Version 1.0
 */
public abstract class AbstractCacheOps {

	protected AbstractCacheOps(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	private final ProceedingJoinPoint joinPoint;

	/**
	 * 织入方法
	 * @return ProceedingJoinPoint
	 */
	public ProceedingJoinPoint joinPoint() {
		return joinPoint;
	}

	/**
	 * 检查缓存数据是否是空值
	 * @param cacheData
	 * @return
	 */
	public boolean nullValue(Object cacheData) {
		return CachePropertiesHolder.nullValue().equals(cacheData);
	}

}
