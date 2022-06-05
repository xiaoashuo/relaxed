package com.relaxed.autoconfigure.web.batch;

import com.relaxed.common.core.batch.AbstractBatchOps;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yakir
 * @Topic BatchOps
 * @Description
 * @date 2022/6/5 17:41
 * @Version 1.0
 */
public class BatchOps extends AbstractBatchOps {

	@Override
	protected Executor executor() {
		return Executors.newScheduledThreadPool(20);
	}

}
