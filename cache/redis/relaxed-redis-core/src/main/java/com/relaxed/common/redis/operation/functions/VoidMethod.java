package com.relaxed.common.redis.operation.functions;

/**
 * 无返回值的函数式接口。 用于执行不需要返回值的操作，主要用于缓存删除等场景。
 *
 * @author Hccake
 * @since 1.0
 */
@FunctionalInterface
public interface VoidMethod {

	/**
	 * 执行操作 该方法不返回任何值，仅执行指定的操作。
	 */
	void run();

}
