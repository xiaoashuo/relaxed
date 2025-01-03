package com.relaxed.common.core.util.batch.funcs;

import com.relaxed.common.core.util.batch.core.BatchMeta;

import java.util.Map;

/**
 * 异常处理器，可在处理完异常后再次抛出异常
 *
 * @author hccake
 */
@FunctionalInterface
public interface ExceptionHandler {

	/**
	 * 处理异常
	 * @param meta 批次元数据
	 * @param extParam 扩展参数 暂时仅有rowIndex
	 * @param throwable 待处理的异常
	 */
	void handle(BatchMeta meta, Map<String, Object> extParam, Throwable throwable);

}
