package com.relaxed.common.core.util.batch.core;

/**
 * 批量处理的位置计算器接口。 用于计算每个批次的起始位置，支持自定义分页策略。 常用于数据库分页查询、文件分片读取等场景。
 *
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface LocationComputer {

	/**
	 * 计算批次的起始位置
	 * @param groupNo 当前批次的组号（从1开始）
	 * @param size 每批次的大小
	 * @return 当前批次的起始位置
	 */
	int compute(int groupNo, int size);

}
