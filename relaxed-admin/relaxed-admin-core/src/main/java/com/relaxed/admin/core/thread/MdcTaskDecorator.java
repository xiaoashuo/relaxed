package com.relaxed.admin.core.thread;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC任务装饰器 用于在线程池中传递MDC上下文信息 确保异步任务能够正确获取和传递日志上下文 主要作用是将主线程的MDC上下文透传至子线程
 *
 * @author Yakir
 * @since 1.0
 */
public class MdcTaskDecorator implements TaskDecorator {

	/**
	 * 装饰Runnable任务 在任务执行前保存当前MDC上下文 在任务执行时恢复主线程的MDC上下文 在任务执行后恢复原来的MDC上下文
	 * @param runnable 原始任务
	 * @return 装饰后的任务
	 */
	@Override
	public Runnable decorate(Runnable runnable) {
		// 获取主线程的MDC上下文
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		return () -> {
			// 保存当前线程的MDC上下文
			Map<String, String> old = MDC.getCopyOfContextMap();
			try {
				// 恢复主线程的MDC上下文
				if (MapUtil.isNotEmpty(contextMap)) {
					MDC.setContextMap(contextMap);
				}
				// 执行原始任务
				runnable.run();
			}
			finally {
				// 恢复原来的MDC上下文
				if (old == null) {
					MDC.clear();
				}
				else {
					MDC.setContextMap(old);
				}
			}
		};
	}

}
