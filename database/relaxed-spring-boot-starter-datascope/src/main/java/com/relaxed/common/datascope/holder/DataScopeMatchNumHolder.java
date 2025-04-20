package com.relaxed.common.datascope.holder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据范围匹配计数器
 * <p>
 * 用于记录 SQL 执行过程中数据权限匹配的次数。 通过 ThreadLocal 确保线程安全，支持在 SQL 解析前后进行计数操作。
 */
public final class DataScopeMatchNumHolder {

	private DataScopeMatchNumHolder() {
	}

	private static final ThreadLocal<AtomicInteger> matchNumTreadLocal = new ThreadLocal<>();

	/**
	 * 初始化匹配计数器
	 * <p>
	 * 在每次 SQL 执行解析前调用，将匹配次数重置为 0。
	 */
	public static void initMatchNum() {
		matchNumTreadLocal.set(new AtomicInteger());
	}

	/**
	 * 获取当前匹配次数
	 * <p>
	 * 返回当前 SQL 解析后被数据权限匹配中的次数。
	 * @return 匹配次数
	 */
	public static int getMatchNum() {
		return matchNumTreadLocal.get().get();
	}

	/**
	 * 增加匹配次数
	 * <p>
	 * 如果存在计数器，则将匹配次数加 1。
	 */
	public static void incrementMatchNumIfPresent() {
		Optional.ofNullable(matchNumTreadLocal.get()).ifPresent(AtomicInteger::incrementAndGet);
	}

	/**
	 * 清除匹配计数器
	 * <p>
	 * 在 SQL 执行解析后调用，移除当前线程的计数器。
	 */
	public static void remove() {
		matchNumTreadLocal.remove();
	}

}
