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
 * 可追踪的 MethodJobHandler 实现类。 继承自 IJobHandler，为 XXL-Job 任务执行添加追踪功能。 主要功能包括： 1.
 * 为每个任务执行生成唯一的 traceId 2. 通过 MDC 上下文传递 traceId 3. 支持任务执行链路追踪
 *
 * @author Yakir
 * @since 1.0
 * @see MethodJobHandler
 */
@Slf4j
public class TraceMethodJobHandler extends IJobHandler {

	/**
	 * MDC 上下文中的 traceId 键名
	 */
	private final static String TRACE_ID = "traceId";

	/**
	 * 被包装的 MethodJobHandler 实例
	 */
	private final MethodJobHandler methodJobHandler;

	/**
	 * 构造函数
	 * @param target 被包装的 MethodJobHandler 实例
	 */
	public TraceMethodJobHandler(MethodJobHandler target) {
		this.methodJobHandler = target;
	}

	/**
	 * 生成 traceId。 默认使用 jobId 拼接无下划线的 UUID 作为 traceId。 如果无法获取 jobId，则使用 "unknown" 作为前缀。
	 * @return 生成的 traceId
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

	/**
	 * 执行任务。 在执行前设置 traceId，执行完成后清理 traceId。
	 * @param param 任务参数
	 * @return 任务执行结果
	 * @throws Exception 执行过程中可能抛出的异常
	 */
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
