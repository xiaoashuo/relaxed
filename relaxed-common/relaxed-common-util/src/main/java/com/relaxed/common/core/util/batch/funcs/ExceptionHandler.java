package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.core.BatchMeta;

import java.util.Map;

/**
 * 批量处理任务的异常处理器接口。 用于处理在批量数据处理过程中出现的异常情况。 实现类可以选择记录异常、重试操作或者在必要时重新抛出异常。
 *
 * @author hccake
 * @since 1.0
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * 处理批量任务执行过程中出现的异常
	 * @param meta 批次元数据，包含当前批次的基本信息（如批次号、起始位置、大小等）
	 * @param extParam 扩展参数，包含额外的上下文信息。目前包含： - rowIndex: 发生异常的数据行索引 - data:
	 * 发生异常的数据内容（JSON格式）
	 * @param throwable 需要处理的异常对象
	 */
	void handle(BatchMeta meta, Map<String, Object> extParam, Throwable throwable);

}
