package com.relaxed.common.core.batch;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Yakir
 * @Topic BatchOps
 * @Description
 * @date 2022/10/21 13:44
 * @Version 1.0
 */
public class BatchOps extends AbstractBatchOps {

	/**
	 * 核心线程池大小
	 */
	private static int coreSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 最大线程池大小
	 */
	private static int maxSize = coreSize * 2 + 1;

	/**
	 * 线程池
	 */
	private static ThreadPoolExecutor executor = ThreadUtil.newExecutor(coreSize, maxSize);

	@Override
	protected Executor executor() {
		return executor;
	}

}
