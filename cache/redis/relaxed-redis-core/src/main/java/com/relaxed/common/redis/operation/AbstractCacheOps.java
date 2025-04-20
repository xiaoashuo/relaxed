package com.relaxed.common.redis.operation;

import com.relaxed.common.redis.config.CachePropertiesHolder;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 缓存操作的抽象基类。 提供缓存操作的基本功能，包括切面连接点的管理和空值检查。
 *
 * @author Yakir
 * @since 1.0
 */
public abstract class AbstractCacheOps {

	/**
	 * 切面连接点
	 */
	private final ProceedingJoinPoint joinPoint;

	/**
	 * 构造函数
	 * @param joinPoint 切面连接点
	 */
	protected AbstractCacheOps(ProceedingJoinPoint joinPoint) {
		this.joinPoint = joinPoint;
	}

	/**
	 * 获取切面连接点
	 * @return 切面连接点对象
	 */
	public ProceedingJoinPoint joinPoint() {
		return joinPoint;
	}

	/**
	 * 检查缓存数据是否是空值标识
	 * @param cacheData 缓存数据
	 * @return 如果是空值标识返回true，否则返回false
	 */
	public boolean nullValue(Object cacheData) {
		return CachePropertiesHolder.nullValue().equals(cacheData);
	}

}
