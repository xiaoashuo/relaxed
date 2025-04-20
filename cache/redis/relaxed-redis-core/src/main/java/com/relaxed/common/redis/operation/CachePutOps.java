package com.relaxed.common.redis.operation;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.function.Consumer;

/**
 * 缓存更新操作类。 封装缓存更新的相关操作，继承自AbstractCacheOps。
 *
 * @author Hccake
 * @since 1.0
 */
public class CachePutOps extends AbstractCacheOps {

	/**
	 * 更新缓存的具体操作方法
	 */
	private Consumer<Object> cachePut;

	/**
	 * 构造函数
	 * @param joinPoint 切面连接点
	 * @param cachePut 更新缓存的具体操作方法
	 */
	public CachePutOps(ProceedingJoinPoint joinPoint, Consumer<Object> cachePut) {
		super(joinPoint);
		this.cachePut = cachePut;
	}

	/**
	 * 获取更新缓存的操作方法
	 * @return 更新缓存的操作方法
	 */
	public Consumer<Object> cachePut() {
		return cachePut;
	}

}
