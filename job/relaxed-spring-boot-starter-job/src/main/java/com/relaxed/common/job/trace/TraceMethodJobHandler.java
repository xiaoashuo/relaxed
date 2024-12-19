package com.relaxed.common.job.trace;

/**
 * @author Yakir
 * @Topic TraceMethodJobHandler
 * @Description
 * @date 2024/12/19 11:28
 * @Version 1.0
 */
import java.lang.reflect.Field;
import java.util.UUID;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import com.xxl.job.core.thread.JobThread;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.ReflectionUtils;

/**
 * 可追踪的 MethodJobHandler.
 *
 * @author Yakir
 * @see MethodJobHandler
 */
@Slf4j
public class TraceMethodJobHandler extends IJobHandler {

	private final static String TRACE_ID = "traceId";

	private final MethodJobHandler methodJobHandler;

	public TraceMethodJobHandler(MethodJobHandler target) {
		this.methodJobHandler = target;
	}

	/**
	 * 生成 traceId, 默认使用 jobId 拼接无下划线的 UUID
	 * @return traceId
	 */
	protected String generateTraceId() {
		Thread thread = Thread.currentThread();
		String jobId;
		if (thread instanceof JobThread) {
			try {
				Field field = JobThread.class.getDeclaredField("jobId");
				field.setAccessible(true);
				Object o = field.get(thread);
				jobId = String.valueOf(o);
			}
			catch (Exception e) {
				jobId = "error";
				log.error("通过工作线程获取jobId失败,当前线程id:{},线程名称:{}", thread.getId(), thread.getName());
			}
		}
		else {
			// 未知jobId
			jobId = "unknown";
		}

		String simpleUUID = UUID.randomUUID().toString().replace("-", "");
		return jobId + "-" + simpleUUID;
	}

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		MDC.put(TRACE_ID, generateTraceId());
		try {
			return this.methodJobHandler.execute(param);
		}
		finally {
			MDC.remove(TRACE_ID);
		}
	}

}
