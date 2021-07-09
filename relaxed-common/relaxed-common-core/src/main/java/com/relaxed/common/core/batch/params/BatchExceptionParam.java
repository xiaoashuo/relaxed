package com.relaxed.common.core.batch.params;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yakir
 * @Topic 数据异常参数
 * @Description
 * @date 2021/7/9 18:16
 * @Version 1.0
 */
@Accessors(chain = true)
@Data
public class BatchExceptionParam {

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 分组参数
	 */
	private BatchGroup batchGroup;

	/**
	 * 当前执行步骤起始位置
	 */
	private int currentStepPosition;

	/**
	 * 异常信息
	 */
	private Throwable throwable;

	public static BatchExceptionParam of(String taskName, Integer currentStepPosition, BatchGroup batchGroup,
			Throwable throwable) {
		BatchExceptionParam batchExceptionParam = new BatchExceptionParam();
		batchExceptionParam.setTaskName(taskName);
		batchExceptionParam.setBatchGroup(batchGroup);
		batchExceptionParam.setCurrentStepPosition(currentStepPosition);
		batchExceptionParam.setThrowable(throwable);
		return batchExceptionParam;
	}

}
