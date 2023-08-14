package com.relaxed.common.core.util.batch;

/**
 * @author Yakir
 * @Topic BatchTask
 * @Description 批次任务
 * @date 2023/8/9 13:52
 * @Version 1.0
 */
public class BatchTask {

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 是否异步执行
	 */
	private boolean isAsync;

	/**
	 * 任务分组
	 */
	private BatchGroup batchGroup;

}
